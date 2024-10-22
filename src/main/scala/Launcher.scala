import api.{AccessApi, ProfileApi, SwaggerDocs}
import org.apache.pekko
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.server.Directives.*

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Launcher
  @main def main() : Unit =
    implicit val actorSystem : ActorSystem = ActorSystem("claptrap")
    implicit val execCont : ExecutionContext = actorSystem.dispatcher

    val routes =
      AccessApi.routes ~
      SwaggerDocs.routes

    val binding = Http().newServerAt("localhost", 8081).bind(routes)

    StdIn.readLine()
    for(bind <- binding)
      bind.unbind()
    binding.onComplete(_ => actorSystem.terminate())

