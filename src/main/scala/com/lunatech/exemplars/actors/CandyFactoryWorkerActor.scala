package com.lunatech.exemplars.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ ActorContext, Behaviors }
import com.lunatech.exemplars.observability.CandyMetrics
import io.opentelemetry.api.trace.{ Span, Tracer }

object CandyFactoryWorkerActor {
  trait CandyFactoryWorkerCommand
  final case object MakeCandy extends CandyFactoryWorkerCommand

  def apply(tracer: Tracer): Behavior[CandyFactoryWorkerCommand] =
    Behaviors.receive { (context, message) =>
      message match {
        case MakeCandy =>
          val span: Span = CandyMetrics.createSpan(tracer, "Making candy")
          val metric = CandyMetrics.ProductionCandyDurationHistogram.startTimer()

          makeCandy(context)

          span.end
          metric.observeDurationWithExemplar("TraceID", span.getSpanContext.getTraceId)
          Behaviors.same
      }
    }

  private def makeCandy(context: ActorContext[CandyFactoryWorkerCommand]): Unit = {
    context.log.info("Starting to make new candy...")
    val randomTime = scala.util.Random.nextInt(500)
    Thread.sleep(randomTime)
    context.log.info("Candy making finished!")
  }
}
