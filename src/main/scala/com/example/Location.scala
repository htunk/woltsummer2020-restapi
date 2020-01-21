package com.example

import scala.math.{Pi, atan2, cos, sin, sqrt}

case class Location(latitude: Double, longitude: Double) {
  import Location._

  private def haversineDistanceInKm(loc1: Location, loc2: Location): Double = {
    val diffLat = degreesToRadians(loc2.latitude-loc1.latitude)
    val diffLon = degreesToRadians(loc2.longitude-loc1.longitude)

    val radLat1 = degreesToRadians(loc1.latitude)
    val radLat2 = degreesToRadians(loc2.latitude)

    val a = sin(diffLat / 2) * sin(diffLat / 2) +
            sin(diffLon / 2) * sin(diffLon / 2) * cos(radLat1) * cos(radLat2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    earthRadius * c
  }

  def distanceTo(another: Location): Double = {
    haversineDistanceInKm(this, another)
  }
}

object Location {
  val earthRadius = 6371

  def degreesToRadians(deg: Double): Double = {
    deg * Pi / 180
  }
}