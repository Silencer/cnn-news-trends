package com.my.cnntrends.engine

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

import com.my.cnntrends.analyzer.NewsInTrendsAnalyzer
import com.my.cnntrends.engine.AggregatorEngine.NewsInTrendsAnalyzed
import com.my.cnntrends.engine.NewsInTrendsProcessor.{Command, FindNewsInTrends}

class NewsInTrendsProcessor(context: ActorContext[Command]) extends AbstractBehavior[Command] {

  private val storage = AggregatorEngine.storage

  override def onMessage(msg: Command): Behavior[Command] = msg match {
    case FindNewsInTrends(replyTo) =>
      val newsInTrends = NewsInTrendsAnalyzer.analyze(storage.getTrends, storage.getAllNews)
      replyTo ! NewsInTrendsAnalyzed(newsInTrends)
      Behaviors.same
  }
}

object NewsInTrendsProcessor {

  sealed trait Command
  final case class FindNewsInTrends(replyTo: ActorRef[AggregatorEngine.Command]) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new NewsInTrendsProcessor(context))
}

