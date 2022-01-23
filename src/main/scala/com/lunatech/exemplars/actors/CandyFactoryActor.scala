package com.lunatech.exemplars.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.lunatech.exemplars.actors.CandyFactoryWorkerActor.MakeCandy
import io.opentelemetry.api.trace.Tracer

import scala.concurrent.duration.{ DurationDouble, DurationInt }

object CandyFactoryActor {
  trait CandyFactoryCommand

  final case object Start extends CandyFactoryCommand

  final case object OrderCandy extends CandyFactoryCommand

  def apply(tracer: Tracer): Behavior[CandyFactoryCommand] =
    Behaviors.setup { context =>
      val worker = context.spawn(CandyFactoryWorkerActor(tracer), "CandyFactoryWorker")

      Behaviors.receiveMessage {
        case Start => Behaviors.withTimers { timers =>
          timers.startTimerWithFixedDelay(OrderCandy, 0.seconds, 0.5.seconds)
          Behaviors.same
        }
      case OrderCandy => worker ! MakeCandy
        Behaviors.same
      }
  }
}
