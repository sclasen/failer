import play.api.libs.concurrent.Akka
import akka.util.duration._
import play.api.libs.ws.WS
import play.api.{Application, GlobalSettings}

object Global extends GlobalSettings {


  override def onStart(app: Application) {
    println("scheduling self ping")
    Akka.system(app).scheduler.schedule(500 milliseconds, 500 milliseconds) {
      WS.url("http://failer.herokuapp.com/").get().onRedeem {
        resp => println("got response")
      }
    }

  }
}
