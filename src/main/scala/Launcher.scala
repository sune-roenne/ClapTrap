import api.{AccessApi, SwaggerDocs}
import config.ClapTrapConfig
import org.apache.pekko
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.cors.scaladsl.CorsDirectives.cors
import org.apache.pekko.http.scaladsl.server.Directives.*

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Launcher
  @main def main() : Unit =
    val certPath = ClapTrapConfig.Server.Https.Certificate.Path
    implicit val actorSystem : ActorSystem = ActorSystem("claptrap")
    implicit val execCont : ExecutionContext = actorSystem.dispatcher

    val routes = concat(
      pathPrefix("access")(AccessApi.routes),
      SwaggerDocs.routes
    )
    val routesWithCors = cors()(routes)

    val binding = Http().newServerAt("localhost", 8081).bind(routesWithCors)

    StdIn.readLine()
    for(bind <- binding)
      bind.unbind()
    binding.onComplete(_ => actorSystem.terminate())

