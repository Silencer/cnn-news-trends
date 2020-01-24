package com.my.cnntrends.analyzer

import com.my.cnntrends.model._

object NewsInTrendsAnalyzer {

  def analyze(trends: Trends, newsList: NewsList): NewsInTrendList = {
    val trendsWords = trends.items.map(stripText)
    val allNewsItems = newsList.list.flatMap(_.items)
    NewsInTrendList(findNewsItemsByTrends(allNewsItems, trendsWords).toSeq.map(x => NewsInTrend(x._1, x._2)))
  }

  private def findNewsItemsByTrends(items: Seq[NewsItem], trendsWords: Set[String]): Map[String, Seq[NewsItem]] = {
    items.flatMap(item => findNewsItemTrend(item, trendsWords)
      .map(trend => (trend, item)))
      .groupMap(_._1)(_._2)
  }

  private def findNewsItemTrend(item: NewsItem, trendsWords: Set[String]): Option[String] = {
    contains(stripText(item.name), trendsWords)
      .orElse(contains(stripText(item.description), trendsWords))
  }

  private def contains(s: String, words: Set[String]): Option[String] = words.find(s.contains)

  private def stripText(s: String): String = s.replaceAll("[^ a-zA-Z0-9]+", "").toLowerCase
}
