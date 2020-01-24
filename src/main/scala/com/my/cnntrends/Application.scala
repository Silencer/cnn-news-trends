package com.my.cnntrends

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.http.scaladsl.settings.ServerSettings

import com.my.cnntrends.engine.AggregatorEngine

object Application extends HttpApp with JsonProjectionProtocol {

  private val actorSystem = ActorSystem(AggregatorEngine(), "Main")

  private val storage = AggregatorEngine.storage

  override def routes: Route =
    concat(
      path("news") {
        get {
          complete(storage.getAllNews)
        }
      },
      path("trends") {
        get {
          complete(storage.getTrends)
        }
      },
      path("news-in-trends") {
        get {
          complete(storage.getNewsInTrends)
        }
      }
    )

  def main(args: Array[String]): Unit = {
    actorSystem ! AggregatorEngine.Start()
    Application.startServer("0.0.0.0", 8080, ServerSettings(Config()))
  }
}
