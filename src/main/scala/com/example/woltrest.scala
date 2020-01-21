package com.example

import org.scalatra._
import org.scalatra.json._
import org.json4s._


import scala.io.{BufferedSource, Source}

class woltrest extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  private def loadRestaurants(src: String): Array[Restaurant] = {
    val jsonSrc: BufferedSource = Source.fromFile(src)
    val jsonStr: String = jsonSrc.getLines.mkString
    val jsonMap = parse(jsonStr).extract[Map[String, Array[Restaurant]]]
    jsonSrc.close()

    jsonMap("restaurants")
  }

  private def jsonError(msg: String): String = {
    s"""{"message": "$msg"}"""
  }

  private def safeToDouble(numb: String): Double = {
    try {
      numb.toDouble
    }
    catch {
      case _: NumberFormatException => halt(
        400, jsonError("Coordinate was incorrectly formatted")
      )
      case _: NullPointerException  => halt(
        400, jsonError("Coordinate was empty")
      )
    }
  }

  val restaurants: Array[Restaurant] = loadRestaurants("./src/main/resources/restaurants.json")

  before() {
    contentType = formats("json")
  }

  get("/") {
    redirect("/restaurants")
  }

  get("/restaurants") {
    restaurants.toList
  }

  get("/restaurants/search"){
    val query: String = params.getOrElse("q", halt(400, jsonError("Missing parameter q")))
    val latitude: Double = safeToDouble(
      params.getOrElse(key="lat", halt(400, jsonError("Missing parameter lat")))
    )
    val longitude: Double = safeToDouble(
      params.getOrElse(key="lon", halt(400, jsonError("Missing parameter lon")))
    )
    val userLocation = Location(latitude, longitude)


    restaurants.filter(_.search(query))
               .filter(_.withinRange(userLocation, 3)).toList
  }

}
