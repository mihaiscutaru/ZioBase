package io.scalac.labs.iot
package application

import DataTypes._

trait PersistenceManager {
  def getUser(UUID: String): User
  def getCategories(): List[Category]
  def getCourses(category: Category): List[Course]
}

object DummyData extends PersistenceManager {
  override def getCategories(): List[Category] =
    List(Category("WebDevelopment"), Category("DataScience"), Category("MobileDevelopment"))

  override def getCourses(category: Category): List[Course] =
    category.name match {
      case "WebDevelopment" =>
        List(Course(category, "React", 4),
           Course(category, "Node.JS", 2),
           Course(category, "Scala", 5))
      case "DataScience" =>
        List[Course](
          Course(category, "MachineLearning", 3),
          Course(category, "Python For Data Scientists", 4),
          Course(category, "Node.JS", 4)
        )
      case "Mobile Development" =>
        List[Course](
          Course(category, "IOS Spark", 3),
          Course(category, "Flutter", 4),
          Course(category, "Android", 2)
        )
    }

  override def getUser(UUID: String): User = {
    val cat = Category("WebDevelopment")
    val currentCourse = Course(cat, "React", 4)

    val enrolled = List(Course(cat, "React", 4),
      Course(cat, "Node.JS", 2),
      Course(cat, "Scala", 5))

    User(UUID, Student ,currentCourse, enrolled)
  }

}

class dbConnector extends PersistenceManager {
  override def getCategories(): List[Category] = ???

  override def getCourses(category: Category): List[Course] = ???

  override def getUser(UUID: String): User = ???
}
