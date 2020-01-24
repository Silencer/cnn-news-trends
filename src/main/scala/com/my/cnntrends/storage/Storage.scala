package com.my.cnntrends.storage

import com.my.cnntrends.model._

trait Storage {

  def getNewsByChannel(channel: NewsChannel): News

  def getAllNews: NewsList

  def getTrends: Trends

  def getNewsInTrends: NewsInTrendList

  def updateNews(v: News): Unit

  def updateTrends(v: Trends): Unit

  def updateNewsInTrend(list: NewsInTrendList): Unit
}
