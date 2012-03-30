package controllers

import play.api.mvc._
import play.api.libs.concurrent.Akka
import java.util.concurrent.atomic.{AtomicLong, AtomicInteger}
import java.util.Random
import play.api.Play.current

object Application extends Controller {


  val count = new AtomicInteger(0)
  val sleep = new AtomicLong(1)

  def index = Action {
    Async {
      Akka.future {
        val curr = count.incrementAndGet()
        if (curr > 1000) {
          val s = sleep.get()
          Thread.sleep(s)
          if (s > 10000) {
            sleep.addAndGet(new Random().nextInt(100))
          }
        }
        Ok(views.html.index("Your new application is ready."))
      }
    }

  }

}