import play.api.libs.concurrent.Akka
import akka.util.duration._
import play.api.libs.ws.WS
import play.api.{Application, GlobalSettings}
import util.Random

object Global extends GlobalSettings {

  val random = new Random()
  var appl: Application = null

  override def onStart(app: Application) {
    appl = app
    println("scheduling self pings")
    (1 to 100) foreach {
      _ => Akka.system(appl).scheduler.scheduleOnce(random.nextInt(500) milliseconds) {
        ping()
      }

    }
  }


  private def ping() {
    WS.url("http://failer.herokuapp.com/").get().onRedeem {
      resp => println("got response")
      Akka.system(appl).scheduler.scheduleOnce(random.nextInt(500) milliseconds) {
        ping()
      }
    }
  }
}
