package com.my.cnntrends.provider

import org.scalatest.FunSuite
import com.my.cnntrends.model.Trends

class GoogleTrendsProviderTest extends FunSuite {

  test("Load Google trends from local XML file") {
    val xmlUrl = getClass.getResource("/google_trends.xml")
    val trends = GoogleTrendsProvider.fromURL(xmlUrl)

    assert(trends == Trends("Daily Search Trends", Set("Earthquake", "Kyle Kuzma")))
  }
}
