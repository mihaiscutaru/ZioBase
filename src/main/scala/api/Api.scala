package io.scalac.labs.iot
package api

import application.{ApplicationService, VideoStorageHandler}
import domain.error.DomainError
import akka.actor.ActorSystem
import akka.event.Logging._
import akka.http.interop._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult.Complete
import akka.http.scaladsl.server.{PathMatcher, Route}
import zio._

object Api {
  trait Service {
    def routes: Route
  }

  val live: ZLayer[Has[HttpServer.Config] with Has[ActorSystem], Nothing, Api] = ZLayer.fromFunction(env =>
    new Service with JsonSupport with ZIOSupport {

      def routes: Route = login ~ categoryList ~ category ~ storage

      implicit val domainErrorResponse: ErrorResponse[DomainError] = {
        case _ => HttpResponse(StatusCodes.InternalServerError)
      }

      val login : Route =
        pathPrefix("login") {
          logRequestResult(("login", InfoLevel)) {
            pathEnd {
              get {
                val userUUId = JavaUUID.toString()
                complete(ApplicationService.postLogin(userUUId))
              }
            }
          }
        }

      val categoryList: Route =
      pathPrefix("categoryList") {
        logRequestResult(("categoryList", InfoLevel)) {
          pathEnd {
            get {
              complete(ApplicationService.ListCategories)
            }
          }
        }
      }

      val category: Route =
          path(pm = "category" / Segment) { category =>
             logRequestResult("category", InfoLevel) {
            pathEnd {
              get {
                complete(ApplicationService.ListCoursesForCategory(category))
              }
            }
          }
        }

      val storage: Route =
        path(pm = "storage") {
          logRequestResult("category", InfoLevel) {
            pathEnd {
              get {
                complete(VideoStorageHandler.listBuckets)
              }
            }
          }
        }


//      implicit val domai nErrorResponse: ErrorResponse[DomainError] = {
//        case RepositoryError(_) => HttpResponse(StatusCodes.InternalServerError)
//        case ValidationError(_) => HttpResponse(StatusCodes.BadRequest)
//      }
//
//      val deviceRoute: Route =
//        pathPrefix("devices") {
//          logRequestResult(("devices", InfoLevel)) {
//            pathEnd {
//              get {
//                complete(ApplicationService.getDevices("Brasov").provide(env))
//              }
//            }
//          }
//        }
//
//      val itemRoute: Route =
//        pathPrefix("items") {
//          logRequestResult(("items", InfoLevel)) {
//            pathEnd {
//              get {
//                complete(ApplicationService.getItems.provide(env))
//              } ~
//                post {
//                  entity(Directives.as[CreateItemRequest]) { req =>
//                    ApplicationService
//                      .addItem(req.name, req.price)
//                      .provide(env)
//                      .map { id =>
//                        complete {
//                          Item(id, req.name, req.price)
//                        }
//                      }
//                  }
//                }
//            } ~
//              path(LongNumber) {
//                itemId =>
//                  delete {
//                    complete(
//                      ApplicationService
//                        .deleteItem(ItemId(itemId))
//                        .provide(env)
//                        .as(JsObject.empty)
//                    )
//                  } ~
//                    get {
//                      complete(ApplicationService.getItem(ItemId(itemId)).provide(env))
//                    } ~
//                    patch {
//                      entity(Directives.as[PartialUpdateItemRequest]) { req =>
//                        complete(
//                          ApplicationService
//                            .partialUpdateItem(ItemId(itemId), req.name, req.price)
//                            .provide(env)
//                            .as(JsObject.empty)
//                        )
//                      }
//                    } ~
//                    put {
//                      entity(Directives.as[UpdateItemRequest]) { req =>
//                        complete(
//                          ApplicationService
//                            .updateItem(ItemId(itemId), req.name, req.price)
//                            .provide(env)
//                            .as(JsObject.empty)
//                        )
//                      }
//                    }
//              }
//          }
//        }  ~
//          pathPrefix("sse" / "items") {
//            import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._
//
//            logRequestResult(("sse/items", InfoLevel)) {
//              pathPrefix("deleted") {
//                get {
//                  complete {
//                    ApplicationService.deletedEvents.toPublisher
//                      .map(p =>
//                        Source
//                          .fromPublisher(p)
//                          .map(itemId => ServerSentEvent(itemId.value.toString))
//                          .keepAlive(1.second, () => ServerSentEvent.heartbeat)
//                      )
//                      .provide(env)
//                  }
//                }
//              }
//            }
//          }   ~
//          pathPrefix("ws" / "items") {
//            logRequestResult(("ws/items", InfoLevel)) {
//              val greeterWebSocketService =
//                Flow[Message].flatMapConcat {
//                  case tm: TextMessage if tm.getStrictText == "deleted" =>
//                    Source.futureSource(
//                      unsafeRunToFuture(
//                        ApplicationService.deletedEvents.toPublisher
//                          .map(p =>
//                            Source
//                              .fromPublisher(p)
//                              .map(itemId => TextMessage(s"deleted: ${itemId.value}"))
//                          )
//                          .provide(env)
//                      )
//                    )
//                  case tm: TextMessage =>
//                    Try(tm.getStrictText.toLong) match {
//                      case Success(value) =>
//                        Source.futureSource(
//                          unsafeRunToFuture(
//                            ApplicationService
//                              .getItem(ItemId(value))
//                              .bimap(
//                                _.asThrowable,
//                                o => Source(o.toList.map(i => TextMessage(i.toString)))
//                              )
//                              .provide(env)
//                          )
//                        )
//                      case Failure(_) => Source.empty
//                    }
//                  case bm: BinaryMessage =>
//                    bm.getStreamedData.runWith(Sink.ignore, env.get[ActorSystem])
//                    Source.empty
//                }
//
//              handleWebSocketMessages(greeterWebSocketService)
//            }
//          }
    }
  )

  // accessors
  val routes: URIO[Api, Route] = ZIO.access[Api](a => Route.seal(a.get.routes))
}
