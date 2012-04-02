package controllers

import play.api.mvc._
import play.api.libs.concurrent.Akka
import java.util.Random
import play.api.Play.current
import java.util.concurrent.atomic.{AtomicBoolean, AtomicLong}

object Application extends Controller {

  val failing = new AtomicBoolean(false)
  val sleep = new AtomicLong(1)

  def index = Action {
    Async {
      Akka.future {
        if (failing.get()) {
          val s = sleep.get()
          Thread.sleep(s)
          if (s < 10000) {
            sleep.addAndGet(new Random().nextInt(100))
          }
        }
        Ok(views.html.index("Application status: Failing = " + failing.get()))
      }
    }
  }


  def failOn = Action {
    failing.set(true)
    Ok(views.html.index("Application status: Failing = " + failing.get()))
  }

  def failOff = Action {
    failing.set(false)
    sleep.set(1)
    Ok(views.html.index("Application status: Failing = " + failing.get()))
  }

}