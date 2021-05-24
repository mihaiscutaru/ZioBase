package io.scalac.labs.iot
package application

import io.circe.Json

import scala.jdk.CollectionConverters._
import io.minio.MinioClient
import zio.{UIO, ZIO}
import io.circe.generic.auto._
import io.circe.syntax._

object VideoStorageHandler {

  val minioClient = new MinioClient("http://localhost:8088/", "minioadmin", "minioadmin")

  def listBuckets(): UIO[String] = {
    UIO(minioClient.listBuckets().asScala.toList.map(_.toString).asJson.toString())
  }

}
