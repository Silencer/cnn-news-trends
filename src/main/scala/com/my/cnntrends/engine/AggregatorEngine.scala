package com.my.cnntrends.engine
import scala.concurrent.duration._

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{Behavior, DispatcherSelector}

import com.my.cnntrends.Config
import com.my.cnntrends.engine.AggregatorEngine._
import com.my.cnntrends.model.{News, NewsInTrendList, Trends}
import com.my.cnntrends.storage.{InMemoryStorage, Storage}

class AggregatorEngine(context: ActorContext[Command]) extends AbstractBehavior[Command] {

  private val storage = AggregatorEngine.storage
  
  private val newsAggregator = context.spawn(NewsAggregator(), s"news-aggregator")
  private val trendsAggregator = context.spawn(TrendsAggregator(), s"trends-aggregator")
  private val trendsAnalyzer = context.spawn(NewsInTrendsProcessor(), s"trends-analyzer")

  implicit val executionContext = context.system.dispatchers.lookup(DispatcherSelector.default())

  override def onMessage(msg: Command): Behavior[Command] = msg match {
    case Start() =>
      context.log.info("Start news aggregator engine")

      context.system.scheduler.schedule(Duration.Zero, Config.getRefreshInterval) {
        context.self ! Aggregate()
      }
      Behaviors.same

    case Aggregate() =>
      context.log.debug("Start news aggregation")
      trendsAggregator ! TrendsAggregator.Get(Config.getGoogleTrendsUrl, context.self)
      Config.getCnnNewsRssUrlList.foreach(item => newsAggregator ! NewsAggregator.Get(item, context.self))
      Behaviors.same

    case NewsAggregated(url, news) =>
      if (storage.getNewsByChannel(news.channel) != news) {
        context.log.debug(s"News aggregated: $url")
        storage.updateNews(news)
        trendsAnalyzer ! NewsInTrendsProcessor.FindNewsInTrends(context.self)
      }
      Behaviors.same

    case TrendsAggregated(url, trends) =>
      if (storage.getTrends != trends) {
        context.log.debug(s"Trends aggregated: $url")
        storage.updateTrends(trends)
        trendsAnalyzer ! NewsInTrendsProcessor.FindNewsInTrends(context.self)
      }
      Behaviors.same

    case NewsInTrendsAnalyzed(list) =>
      context.log.debug(s"News in trends updated")
      storage.updateNewsInTrend(list)
      Behaviors.same
  }
}

object AggregatorEngine {

  sealed trait Command
  final case class Start() extends Command
  final case class Aggregate() extends Command
  final case class NewsAggregated(url: String, news: News) extends Command
  final case class TrendsAggregated(url: String, trends: Trends) extends Command
  final case class NewsInTrendsAnalyzed(list: NewsInTrendList) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new AggregatorEngine(context))

  val storage: Storage = InMemoryStorage
}
