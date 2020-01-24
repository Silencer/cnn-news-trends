package com.my.cnntrends.provider

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.scalatest.FunSuite
import com.my.cnntrends.model
import com.my.cnntrends.model.{NewsChannel, NewsItem}

class CNNNewsProviderTest extends FunSuite {

  private val DATE_TIME_FORMAT = DateTimeFormatter.RFC_1123_DATE_TIME

  test("Load RSS from local XML file") {
    val xmlUrl = getClass.getResource("/cnn_top_stories.xml")
    val news = CNNNewsProvider.fromURL(xmlUrl)

    val expectedChannel = NewsChannel(
      "CNN.com - RSS Channel - App International Edition",
      "CNN.com delivers up-to-the-minute news and information on the latest top stories, weather, entertainment, "
        + "politics and more.",
      "https://www.cnn.com/app-international-edition/index.html")
    assert(news.channel == expectedChannel)

    val expectedItems = Seq(
      NewsItem(
        "Senate approves impeachment trial rules after nasty marathon debate",
        "The Senate early Wednesday morning approved rules for the Senate impeachment trial of President Donald Trump "
          + "on a party-line vote that delays the question of whether the Senate should subpoena witnesses and "
          + "documents until later in the trial.",
        "https://www.cnn.com/2020/01/21/politics/senate-impeachment-trial-day-1/index.html",
        Some(ZonedDateTime.parse("Wed, 22 Jan 2020 07:16:17 GMT", DATE_TIME_FORMAT))),
      model.NewsItem(
        "Chief justice scolds legal teams after tense exchange",
        "",
        "https://www.cnn.com/collections/intl-impeachment-0122/",
        Some(ZonedDateTime.parse("Wed, 22 Jan 2020 08:38:15 GMT", DATE_TIME_FORMAT))))
    assert(news.items == expectedItems)
  }
}
