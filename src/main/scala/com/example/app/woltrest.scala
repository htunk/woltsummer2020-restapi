package com.example.app

import org.scalatra._

class woltrest extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
