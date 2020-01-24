package com.my.cnntrends.provider

import java.net.URL
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import scala.util.Try
import scala.xml.{Node, XML}

import com.my.cnntrends.model
import com.my.cnntrends.model.{News, NewsChannel, NewsItem}
import com.typesafe.scalalogging.StrictLogging

object CNNNewsProvider extends NewsProvider with StrictLogging {

  private val DATE_TIME_FORMAT = DateTimeFormatter.RFC_1123_DATE_TIME

  def fromURL(url: URL): News = {
    logger.debug(s"Retrieving CNN RSS from URL: $url")
    val document = XML.load(url)
    val channelNode = (document \ "channel").head
    model.News(convertChannel(channelNode), (channelNode \ "item").map(convertItem))
  }

  def convertChannel(channelNode: Node): NewsChannel = {
    val title = stripText((channelNode \ "title").text)
    val description = stripText((channelNode \ "description").text)
    val link = (channelNode \ "link").text
    NewsChannel(title, description, link)
  }

  def convertItem(itemNode: Node): NewsItem = {
    val title = stripText((itemNode \ "title").text)
    val description = stripText((itemNode \ "description").text)
    val link = (itemNode \ "link").text
    val dateTime = Try(ZonedDateTime.parse((itemNode \ "pubDate").text, CNNNewsProvider.DATE_TIME_FORMAT)).toOption
    model.NewsItem(title, description, link, dateTime)
  }

  def stripText(s: String): String = s.replaceAll("<[^>]*>", "")
    .replaceAll("\\n", "")
    .strip()

}
