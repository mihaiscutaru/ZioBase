package DataTypes

case class Category(name: String)
case class Course(category: Category, name: String, rating: Int)

