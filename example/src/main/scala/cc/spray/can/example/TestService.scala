/*
 * Copyright (C) 2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.spray.can
package example

import akka.actor.{Kill, Actor}
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

class TestService(id: String) extends Actor {
  val log = LoggerFactory.getLogger(getClass)
  self.id = id

  val iso88591 = Charset.forName("ISO-8859-1")
  val headers = List(HttpHeader("Server", "spray-can/test"), HttpHeader("Content-Type", "text/plain"))

  def response(msg: String, status: Int = 200) = HttpResponse(status, headers, msg.getBytes(iso88591))

  protected def receive = {

    case HttpRequest("GET", "/", _, _, _, complete) => complete {
      response("Say hello to a spray-can app")
    }

    case HttpRequest("POST", "/crash", _, _, _, complete) => {
      complete(response("Hai! (about to kill the HttpServer)"))
      self ! Kill
    }

    case HttpRequest("POST", "/stop", _, _, _, complete) => {
      complete(response("Shutting down the spray-can server..."))
      Actor.registry.shutdownAll()
    }

    case HttpRequest("GET", "/stats", _, _, _, complete) => {
      val serverActor = Actor.registry.actorsFor("spray-can-server").head
      (serverActor ? GetServerStats).mapTo[ServerStats].onComplete {
        _.value.get match {
          case Right(stats) => complete {
            response {
              "Uptime: " + (stats.uptime / 1000.0) + " sec\n" +
              "Requests dispatched: " + stats.requestsDispatched + '\n'
            }
          }
          case Left(ex) => complete(response("Couldn't get server stats due to " + ex, status = 500))
        }
      }
    }

    case HttpRequest(_, _, _, _, _, complete) => complete(response("Unknown command!", 404))
  }
}