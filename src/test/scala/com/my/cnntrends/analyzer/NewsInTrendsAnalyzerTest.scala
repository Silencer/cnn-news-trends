package com.my.cnntrends.analyzer

import org.scalatest.FunSuite
import com.my.cnntrends.model.{News, NewsChannel, NewsInTrend, NewsInTrendList, NewsItem, NewsList, Trends}

class NewsInTrendsAnalyzerTest extends FunSuite {

  val newsChannel1 = NewsChannel("channel-1", "channel-1", "http://channel-1")

  val newsChannel2 = NewsChannel("channel-2", "channel-2", "http://channel-2")

  val newsItem1 = NewsItem(
    "Mr. Peanut is dead for some reason",
    "Mr. Peanut, the 104-year old mascot of the Planters snack food company, has died after sacrificing himself " +
      "in what appears to be a traumatic road accident.",
    "https://peanut",
    None)

  val newsItem2 = NewsItem(
    "Barcelona 'avoids total ridicule' in narrow win over Ibiza",
    "Barcelona proved to be a real party pooper on Wednesday, coming from behind to beat Ibiza in the Copa del " +
      "Rey thanks to two late goals from Antoine Griezmann.",
    "https://barcelona",
    None)

  val newsList = NewsList(Seq(
    News(newsChannel1, Seq(newsItem1)),
    News(newsChannel2, Seq(newsItem2))
  ))

  val trendPeanut = "Mr. Peanut"

  val trendBarcelona = "Barcelona"

  test("Analyze news in trends for news item 1") {
    val trends = Trends("trends", Set(trendPeanut))
    val newsInTrendList = NewsInTrendsAnalyzer.analyze(trends, newsList)
    assert(newsInTrendList == NewsInTrendList(Seq(NewsInTrend("mr peanut", Seq(newsItem1)))))
  }

  test("Analyze news in trends for news item 2") {
    val trends = Trends("trends", Set(trendBarcelona))
    val newsInTrendList = NewsInTrendsAnalyzer.analyze(trends, newsList)
    assert(newsInTrendList == NewsInTrendList(Seq(NewsInTrend("barcelona", Seq(newsItem2)))))
  }

  test("Analyze news in trends for news item 1 and 2") {
    val trends = Trends("trends", Set(trendPeanut, trendBarcelona))
    val newsInTrendList = NewsInTrendsAnalyzer.analyze(trends, newsList)
    assert(newsInTrendList == NewsInTrendList(Seq(
      NewsInTrend("mr peanut", Seq(newsItem1)),
      NewsInTrend("barcelona", Seq(newsItem2))
    )))
  }
}
