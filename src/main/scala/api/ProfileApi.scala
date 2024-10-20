package api

import org.apache.pekko.stream.scaladsl.*
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.server.*
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import spray.json.DefaultJsonProtocol.*

import spray.json.RootJsonFormat


import scala.concurrent.Future

object ProfileApi {

  given profileFormat : RootJsonFormat[ProfileDto] = jsonFormat2(ProfileDto.apply)

  val routes : Route = concat(
    path("profile") {
      get {
        complete(ProfileDto("Hello", "Buddy"))
      }
    },
    path("profiles"){
      get {
        complete(ProfileDto("Another", "Hello"))
      }
    }
  )


  case class ProfileDto(
                       Text : String,
                       OtherText : String
                       )


}
