package com.example

case class Restaurant(
                     city: String,
                     currency: String,
                     delivery_price: Int,
                     description: String,
                     image: String,
                     location: Array[Double],
                     name: String,
                     online: Boolean,
                     tags: Array[String],
                     blurhash: String
                     ) {
  val _location = Location(location(1), location(0))

  def search(key: String): Boolean = {
    (name contains key) || (description contains key) || tags.exists(_ contains key)
  }

  def withinRange(usrLocation: Location, range: Int): Boolean = {
    this._location.distanceTo(usrLocation) <= range
  }
}
