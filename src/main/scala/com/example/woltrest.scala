package com.example

import org.scalatra._
import org.scalatra.json._
import org.json4s._

import scala.io.{BufferedSource, Source}

class woltrest extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  val jsonSrc: BufferedSource = Source.fromFile("./src/main/resources/restaurants.json")
  val jsonStr: String = jsonSrc.getLines.mkString
  val jsonMap = parse(jsonStr).extract[Map[String, Array[Restaurant]]]
  jsonSrc.close()

  val restaurants = jsonMap("restaurants")





  before() {
    contentType = formats("json")
  }


  get("/") {
    "helloboi" //TODO: Redirect to /restaurants
  }

  get("/restaurants/search"){
    val query: String = params.getOrElse("q", halt(400))
    val latitude: Double = params.getOrElse(key="lat", halt(400)).toDouble //TODO: Catch errors
    val longitude: Double = params.getOrElse(key="lon", halt(400)).toDouble

    val userLocation = Location(latitude, longitude)

    restaurants.filter(_.search(query))
               .filter(_.withinRange(userLocation, 3)).toList
  }

}
