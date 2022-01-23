package com.lunatech.exemplars.observability

import io.opentelemetry.api.trace.{ Span, Tracer }
import io.opentelemetry.context.Context
import io.prometheus.client.Histogram

object CandyMetrics {
  final val ProductionCandyDurationHistogram: Histogram =
    Histogram
      .build()
      .namespace("lunatech")
      .subsystem("factory")
      .name("production_candy_duration")
      .help("Amount of time it takes to make one candy")
      .withExemplars()
      .register()


  def createSpan(tracer: Tracer, operationName: String): Span =
    tracer
      .spanBuilder(operationName)
      .setParent(Context.current())
      .startSpan()
}
