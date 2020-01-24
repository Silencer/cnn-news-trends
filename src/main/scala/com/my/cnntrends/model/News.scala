package com.my.cnntrends.model

case class News(
  channel: NewsChannel,
  items: Seq[NewsItem]
)
