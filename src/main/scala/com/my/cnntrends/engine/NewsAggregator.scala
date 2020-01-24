package com.my.cnntrends.engine

import java.net.URL

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}

import com.my.cnntrends.engine.AggregatorEngine.NewsAggregated
import com.my.cnntrends.engine.NewsAggregator.{Command, Get}
import com.my.cnntrends.provider.CNNNewsProvider

class NewsAggregator(context: ActorContext[Command]) extends AbstractBehavior[Command] {

  override def onMessage(msg: Command): Behavior[Command] = msg match {
    case Get(url, replyTo) =>
      val news = CNNNewsProvider.fromURL(new URL(url))
      replyTo ! NewsAggregated(url, news)
      Behaviors.same
  }
}

object NewsAggregator {

  sealed trait Command
  final case class Get(url: String, replyTo: ActorRef[AggregatorEngine.Command]) extends Command
  
  def apply(): Behavior[Command] =
    Behaviors.setup(context => new NewsAggregator(context))
}
