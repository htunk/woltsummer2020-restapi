package com.example

import org.scalatra.test.scalatest._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, Formats}

class woltrestTests extends ScalatraFunSuite {
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
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

  test("Search with empty query should return status 400") {
    get("/restaurants/search?q=&lat=60.17&lon=24.93") {
      status should equal (400)
    }
  }

  test("Search with correct parameters should return status 200") {
    get("/restaurants/search?q=burger&lat=60.17&lon=24.93") {
      status should equal (200)
    }
  }


  test("Search with query 'Asenneburgeri' should return exactly one restaurant") {
    get("/restaurants/search?q=Asenneburgeri&lat=60.17045&lon=24.93147") {
      val parsedResponse = parse(body).extract[Array[Restaurant]]
      parsedResponse.length should equal (1)
      parsedResponse.head.description should equal ("Asenneburgeri")
    }
  }

  test("Search with query 'a' with appropriate coordinates should return all (50) restaurants.") {
    get("/restaurants/search?q=a&lat=60.17045&lon=24.93147") {
      val parsedResponse = parse(body).extract[Array[Restaurant]]
      parsedResponse.length should equal (50)
    }
  }


  //These tests are not ran when deploying via Docker
  ignore("Hash with correct parameters should return status 200") {
    get("/restaurants/hash?q=Asenneburger&lat=60.17045&lon=24.93147&x_comp=3&y_comp=4") {
      status should equal(200)
    }
  }

  ignore("Hash with query 'burger', x_comp 3 and y_comp 4 should return 7 hashes, each 28 characters long") {
    get("/restaurants/hash?q=burger&lat=60.17045&lon=24.93147&x_comp=3&y_comp=4") {
      val parsedResponse = parse(body).extract[List[(String, String)]]
      parsedResponse.length should equal (7)
      parsedResponse.forall {
        tuple => tuple._2.length == 28
      } should equal (true)
    }
  }
}
