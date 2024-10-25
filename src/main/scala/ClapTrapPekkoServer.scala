import api.{AccessApi, SwaggerDocs}
import config.ClapTrapConfig
import org.apache.commons.io.{FileUtils, IOUtils}
import org.apache.pekko
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import org.apache.pekko.http.cors.scaladsl.CorsDirectives.cors
import org.apache.pekko.http.scaladsl.server.Directives.*

import java.io.{ByteArrayInputStream, File}
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object ClapTrapPekkoServer :
  def startServer() : Unit =
    implicit val actorSystem: ActorSystem = ActorSystem(name = "claptrap", config = ClapTrapConfig.config)
    implicit val execCont: ExecutionContext = actorSystem.dispatcher

    val routes = concat(
      pathPrefix("access")(AccessApi.routes),
      SwaggerDocs.routes
    )
    val routesWithCors = cors()(routes)

    val sslContext = setupSslContext
    val httpsContext = setupHttpsContext(sslContext)
    val binding = Http().newServerAt("localhost", ClapTrapConfig.Server.Port)
      .enableHttps(httpsContext)
      .bind(routesWithCors)

//    for (bind <- binding)
//      bind.unbind()
//    binding.onComplete(_ => actorSystem.terminate())

  def setupSslContext : SSLContext =
    val certConf = ClapTrapConfig.Server.Https.Certificate
    val certFile = FileUtils.readFileToByteArray(new File(certConf.Path))
    val certPassword = certConf.Password.toCharArray
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(new ByteArrayInputStream(certFile), certPassword)
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, certPassword)
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(ks)
    val returnee = SSLContext.getInstance("TLS")
    returnee.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom())
    returnee

  def setupHttpsContext(sslContext: SSLContext) : HttpsConnectionContext =
    val returnee = ConnectionContext.httpsServer(() =>
      val engine = sslContext.createSSLEngine()

      engine
    )
    returnee


