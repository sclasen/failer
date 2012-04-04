package controllers

import play.api.mvc._
import play.api.libs.concurrent.Akka
import java.util.Random
import play.api.Play.current
import java.util.concurrent.atomic.{AtomicInteger, AtomicBoolean}

object Application extends Controller {

  val failing = new AtomicBoolean(false)
  val sleep = new AtomicInteger(1)
  val mean = 500
  val stdDev = 1000
  val whileFailingAddPerRequest = 25

  def index = Action {
    Async {
      Akka.future {
        var rand = new Random()
        var think = (mean + (stdDev * rand.nextGaussian().abs)).toLong
        if (failing.get()) {
          val s = sleep.get()
          if (s < 10000) {
            sleep.addAndGet(rand.nextInt(whileFailingAddPerRequest))
          }
          think += s
        }
        Thread.sleep(think)
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