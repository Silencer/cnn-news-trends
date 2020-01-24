package com.my.cnntrends.storage

import java.util.concurrent.ConcurrentHashMap

import scala.jdk.CollectionConverters._

import com.my.cnntrends.model._

object InMemoryStorage extends Storage {

  private val news = new ConcurrentHashMap[NewsChannel, Seq[NewsItem]]()

  private var trends = Trends("Unavailable", Set.empty)

  private var newsInTrends = NewsInTrendList(Seq.empty)

  override def getNewsByChannel(channel: NewsChannel): News = News(channel, news.get(channel))

  override def getAllNews: NewsList =
    NewsList(news.entrySet().asScala.map(entry => News(entry.getKey, entry.getValue)).toSeq)

  override def getNewsInTrends: NewsInTrendList = newsInTrends
  
  override def getTrends: Trends = trends

  override def updateNews(v: News): Unit = news.put(v.channel, v.items)

  override def updateTrends(v: Trends): Unit = { trends = v }

  override def updateNewsInTrend(list: NewsInTrendList): Unit = { newsInTrends = list }   
}
