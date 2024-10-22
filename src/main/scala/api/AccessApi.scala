package api

import jakarta.ws.rs.core.MediaType
import org.apache.pekko.http.scaladsl.*
import org.apache.pekko.http.scaladsl.server.{Directives, Route}
import spray.json.DefaultJsonProtocol.*
import spray.json.{CollectionFormats, JsonWriter, RootJsonFormat, RootJsonWriter}
import jakarta.ws.rs.{Consumes, GET, POST, Path, Produces}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import java.time.LocalDateTime
import java.time.temporal.{TemporalAmount, TemporalUnit}


@Path("/access")
object AccessApi extends Directives with StandardTypeFormats with SprayJsonSupport with CollectionFormats {
  import dateTimeFormat.given

  case class CreateAccessScopeRequest(userId : String, scopeName : String, expiration: LocalDateTime)
  case class AccessScope(userId: String, scopeName : String, publicKey: String, expiration: LocalDateTime)
  given createAccessScopeFormat : RootJsonFormat[CreateAccessScopeRequest] = jsonFormat3(CreateAccessScopeRequest.apply)
  given accessScopeFormat : RootJsonFormat[AccessScope] = jsonFormat4(AccessScope.apply)
  given accessCollectionFormat : RootJsonWriter[Seq[AccessScope]] = seqFormat[AccessScope].write

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/access-scope")
  @Operation(
    summary = "Creates new Access Scope",
    requestBody = new RequestBody(
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[CreateAccessScopeRequest])))
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "New Access Scope", content = Array(new Content(schema = new Schema(implementation = classOf[AccessScope]))))
    )
  )
  def createAccessScope : Route =
    path("access-scope")
      post {
        entity(as[CreateAccessScopeRequest]) {
          request =>
            complete{
              AccessScope(request.userId, request.scopeName, "DFDKSOEMdmgfo302+34032ir´+0fick",  request.expiration)
            }
        }
      }
  @GET
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/access-scopes")
  @Operation(
    summary = "Get all Access Scopes",
    responses = Array(
      new ApiResponse(responseCode = "200", description = "New Access Scope", content = Array(new Content(schema = new Schema(implementation = classOf[AccessScope]))))
    )
  )
  def getAccessScopes : Route =
    path("access-scopes" ) {
      pathEndOrSingleSlash {
        get {
          complete {
            List(
              AccessScope("jdjd92", "scope:scopey", "DFDKSOEMdmgfo302+34032ir´+0fick", LocalDateTime.now())
            )
          }
        }
      }
    }

  def routes : Route = concat(
    getAccessScopes,
    createAccessScope
  )

}

