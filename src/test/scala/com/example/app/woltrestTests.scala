package com.example

import org.scalatra.test.scalatest._

class woltrestTests extends ScalatraFunSuite {

  addServlet(classOf[woltrest], "/*")

  test("GET on / should return status 302") {
    get("/") {
      status should equal (302)
    }
  }

  test("GET on /restaurants should return status 200") {
    get("/restaurants") {
      status should equal (200)
    }
  }

  test("Search without all parameters should return status 400") {
    get("/restaurants/search?q=test&lat=60.17") {
      status should equal (400)
    }
    get("/restaurants/search?q=test&lon=24.93") {
      status should equal (400)
    }
    get("/restaurants/search?lat=60.17&lon=24.93") {
      status should equal (400)
    }
  }

  test("Search with incorrect coordinates should return status 400") {
    get("/restaurants/search?q=burger&lat=asd&lon=24.93") {
      status should equal (400)
    }
  }

  test("Search with correct parameters should return status 200") {
    get("/restaurants/search?q=burger&lat=60.17&lon=24.93") {
      status should equal (200)
    }
  }


}
