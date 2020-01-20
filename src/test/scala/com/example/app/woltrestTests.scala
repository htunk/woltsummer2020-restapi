package com.example.app

import org.scalatra.test.scalatest._

class woltrestTests extends ScalatraFunSuite {

  addServlet(classOf[woltrest], "/*")

  test("GET / on woltrest should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
