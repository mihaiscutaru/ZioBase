package DataTypes

sealed trait UserCategory
case object Student extends UserCategory
case object Teacher extends UserCategory

case class User(uuid: String, category: UserCategory, currentCourse: Course, enrolled : List[Course])
