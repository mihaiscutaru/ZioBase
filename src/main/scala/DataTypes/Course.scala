package DataTypes

import akka.http.scaladsl.model._

case class Category(name: String)
case class Course(category: Category, name: String, rating: Int)
case class Lecture(videoURI: Uri, progress: Int)
