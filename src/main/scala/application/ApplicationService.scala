package io.scalac.labs.iot
package application

import zio.UIO
import DataTypes._
import io.circe.generic.auto._, io.circe.syntax._

object ApplicationService {

  val pm: PersistenceManager = DummyData


  def ListCategories: UIO[String] = {
    val catList = pm.getCategories().map(_.name).mkString(", ")
    UIO(catList)
  }

  def ListCoursesForCategory(catName: String): UIO[String] = {
    UIO(pm.getCourses(Category(name = catName)).asJson.noSpaces)
  }

  def postLogin(uuid: String): UIO[String] = {
    UIO(pm.getUser(uuid).asJson.noSpaces)
    //TODO populate user specific settings,
  }

  def ListLecturesForCourse(courseName: String ) = ???

}
