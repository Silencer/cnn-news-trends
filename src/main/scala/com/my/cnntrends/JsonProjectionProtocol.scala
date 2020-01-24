package com.my.cnntrends

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import com.my.cnntrends.model._
import spray.json._

trait JsonProjectionProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object ZonedDateTimeFormat extends JsonFormat[ZonedDateTime] {

    override def write(obj: ZonedDateTime): JsValue = JsString(obj.format(DateTimeFormatter.ISO_DATE_TIME))

    override def read(json: JsValue): ZonedDateTime = ZonedDateTime.parse(json.toString(), DateTimeFormatter.ISO_DATE_TIME)
  }

  implicit val newsChannelFormat = jsonFormat3(NewsChannel)
  implicit val newsItemsFormat = jsonFormat4(NewsItem)
  implicit val newsFormat = jsonFormat2(News)
  implicit val newsListFormat = jsonFormat1(NewsList)
  implicit val trendsFormat = jsonFormat2(Trends)
  implicit val newsInTrendFormat = jsonFormat2(NewsInTrend)
  implicit val newsInTrendListFormat = jsonFormat1(NewsInTrendList)
}