package com.my.cnntrends.engine

import java.net.URL

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

import com.my.cnntrends.engine.AggregatorEngine.TrendsAggregated
import com.my.cnntrends.engine.TrendsAggregator.{Command, Get}
import com.my.cnntrends.provider.GoogleTrendsProvider

class TrendsAggregator(context: ActorContext[Command]) extends AbstractBehavior[Command] {

  override def onMessage(msg: Command): Behavior[Command] = msg match {
    case Get(url, replyTo) =>
      val news = GoogleTrendsProvider.fromURL(new URL(url))
      replyTo ! TrendsAggregated(url, news)
      Behaviors.same
  }
}

object TrendsAggregator {

  sealed trait Command
  final case class Get(url: String, replyTo: ActorRef[AggregatorEngine.Command]) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new TrendsAggregator(context))
}

