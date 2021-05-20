package io.scalac.labs.iot

import akka.actor.ActorSystem
import akka.http.interop.HttpServer
import akka.http.scaladsl.server.Route
import com.typesafe.config.{Config, ConfigFactory}
import io.scalac.labs.iot.api.Api
import io.scalac.labs.iot.config.AppConfig
import zio._
import zio.config.typesafe.TypesafeConfig
import zio.console.putStrLn

object Boot extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    ZIO(ConfigFactory.load.resolve)
      .flatMap(rawConfig => program().provideCustomLayer(prepareEnvironment(rawConfig)))
      .exitCode
  }

  private def program(): RIO[HttpServer with ZEnv, Unit] = {
    val startHttpServer = HttpServer.start.tapM(_ => putStrLn("Server online."))
    startHttpServer.useForever
  }

  private def prepareEnvironment(rawConfig: Config): TaskLayer[HttpServer] = {
    val configLayer = TypesafeConfig.fromTypesafeConfig(rawConfig, AppConfig.descriptor)

    // narrowing down to the required part of the config to ensure separation of concerns
    val apiConfigLayer = configLayer.map(c => Has(c.get.api))

    val actorSystemLayer: TaskLayer[Has[ActorSystem]] = ZLayer.fromManaged {
      ZManaged.make(ZIO(ActorSystem("zio-base")))(s => ZIO.fromFuture(_ => s.terminate()).either)
    }

    val apiLayer: TaskLayer[Api] = (apiConfigLayer ++ actorSystemLayer) >>> Api.live

    val routesLayer: URLayer[Api, Has[Route]] =
      ZLayer.fromService[Api.Service, Route] { (api) =>
        api.routes
      }

    val serverEnv: TaskLayer[HttpServer] = {
      (actorSystemLayer ++ apiConfigLayer ++ (apiLayer >>> routesLayer)) >>> HttpServer.live
    }

    serverEnv
  }
}
