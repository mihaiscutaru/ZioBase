package io.scalac.labs.iot
package application

import zio.UIO
import io.circe.generic.auto._,  io.circe.syntax._

object ApplicationService {
  def hello: UIO[String] = UIO("Hello world")

  def ListCategories: UIO[String] = {
    val catList = DummyData.getCategories().map(_.name).mkString(", ")
    UIO(catList)
  }
  def ListCoursesForCategory(catName:String): UIO[String] =
  {
    val json = DummyData.getCourses(Category(name=catName)).asJson.noSpaces
    UIO(json)
}
}
