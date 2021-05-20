package io.scalac.labs.iot
package application

import zio.UIO

object ApplicationService {
  def hello: UIO[String] = UIO("Hello world")
}
