package com.my.cnntrends.model

import java.time.ZonedDateTime

case class NewsItem(
  name: String,
  description: String,
  url: String,
  dateTime: Option[ZonedDateTime]
)
