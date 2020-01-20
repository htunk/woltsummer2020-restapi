package com.example

import org.scalatra._
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}

class woltrest extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }


  get("/") {
    List("asd", "gasd", "basd")
  }

}
