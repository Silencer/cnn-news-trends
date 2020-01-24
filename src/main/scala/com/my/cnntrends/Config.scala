package com.my.cnntrends

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.jdk.CollectionConverters._

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  private val config = ConfigFactory.load()

  def apply(): Config = config

  def getRefreshInterval: FiniteDuration = Duration.fromNanos(config.getDuration("app.refresh-interval").toNanos)

  def getGoogleTrendsUrl: String = config.getString("app.google-trends-url")

  def getCnnNewsRssUrlList: List[String] = config.getStringList("app.cnn-news-rss").asScala.toList
}
