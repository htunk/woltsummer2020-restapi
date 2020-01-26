package com.example

import org.scalatra._
import org.scalatra.json._
import org.json4s._
import scalaj.http.Http

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
        400, jsonError("Parameters were incorrectly formatted")
      )
      case _: NullPointerException  => halt(
        400, jsonError("Parameter was missing")
      )
    }
  }

  private def searchRestaurants(params: Params): List[Restaurant] = {
    val query: String = params.getOrElse("q", halt(400, jsonError("Missing parameter q")))
    if (query.isEmpty) halt(400, jsonError("Parameter q was empty"))
    val latitude: Double = safeToDouble(
      params.getOrElse(key="lat", halt(400, jsonError("Missing parameter lat")))
    )
    val longitude: Double = safeToDouble(
      params.getOrElse(key="lon", halt(400, jsonError("Missing parameter lon")))
    )
    val userLocation: Location = Location(latitude, longitude)

    restaurants.filter(_.withinRange(userLocation, 3))
               .filter(_.search(query)).toList
  }

  private def hashRestaurants(params: Params): List[(String, String)] = {
    val xComp: Double = safeToDouble(
      params.getOrElse(key="x_comp", halt(400, jsonError("Missing parameter x_comp")))
    )
    val yComp: Double = safeToDouble(
      params.getOrElse(key="y_comp", halt(400, jsonError("Missing parameter y_comp")))
    )
    val urls: List[String] = searchRestaurants(params).map(_.image)

    val ret = urls.par.map(url => {
      val hash = getBlurhash(url, xComp.toInt, yComp.toInt)
      (url, hash)
    })

    ret.toList
  }

  private def getBlurhash(url: String, xComponents: Int, yComponents: Int): String = {
    val host = sys.env.getOrElse("HOST", "127.0.0.1")
    Http(s"http://$host:5000/hash").params(Map(
      "url" -> url,
      "x_comp" -> xComponents.toString,
      "y_comp" -> yComponents.toString)
    ).asString.body
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

  get("/restaurants/hash") {
    hashRestaurants(params)
  }

  get("/restaurants/search"){
    searchRestaurants(params)
  }
}
