package com.my.cnntrends.model

case class NewsInTrend(
  trend: String,
  news: Seq[NewsItem]
)
