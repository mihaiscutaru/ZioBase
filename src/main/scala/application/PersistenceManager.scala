package io.scalac.labs.iot
package application

case class Category(name: String)
case class Course(category: Category, name: String, rating: Int)

trait PersistenceManager {
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
}

class dbConnector extends PersistenceManager {
    override def getCategories(): List[Category] = ???

  override def getCourses(category: Category): List[Course] = ???
}
