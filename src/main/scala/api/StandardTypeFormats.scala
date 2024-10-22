package api

import org.apache.pekko.stream.FlowMonitorState.Failed

import java.time.LocalDateTime
import spray.json.{JsString, JsValue, JsonFormat, deserializationError}

import scala.util.{Failure, Success, Try}
import java.time.format.DateTimeFormatter

trait StandardTypeFormats {
  private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
  given dateTimeFormat : JsonFormat[LocalDateTime] with
    def write(date: LocalDateTime): JsString = JsString(date.format(dateTimeFormatter))
    def read(json: JsValue): LocalDateTime = json match
      case JsString(rawString) => Try {
        dateTimeFormatter.parse(rawString)
      } match
        case Success(tim) => LocalDateTime.from(tim)
        case Failure(err) => deserializationError(s"Failed to parse: ${rawString}")
      case v => deserializationError(s"Expected a JSON string, but got ${v} of type: ${v.getClass}")


}
