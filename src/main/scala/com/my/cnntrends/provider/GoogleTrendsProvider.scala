package com.my.cnntrends.provider

import java.net.URL

import scala.xml.XML

import com.my.cnntrends.model.Trends
import com.typesafe.scalalogging.StrictLogging

object GoogleTrendsProvider extends TrendsProvider with StrictLogging {

  def fromURL(url: URL): Trends = {
    logger.debug(s"Retrieving Google trends: $url")
    val document = XML.load(url)
    val channelNode = (document \ "channel").head
    Trends((channelNode \ "title").text, (channelNode \ "item" \ "title").map(_.text).toSet)
  }
}
