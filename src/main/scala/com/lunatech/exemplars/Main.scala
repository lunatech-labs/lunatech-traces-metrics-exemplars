package com.lunatech.exemplars

import akka.actor.typed.ActorSystem
import com.lunatech.exemplars.actors.CandyFactoryActor
import com.lunatech.exemplars.actors.CandyFactoryActor.{ CandyFactoryCommand, Start }
import com.lunatech.exemplars.observability.Tracing
import io.opentelemetry.api.trace.Tracer
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports

object Main extends App {

  // Metrics
  val prometheusPort = 13798
  new HTTPServer(prometheusPort)
  DefaultExports.initialize()

  // Traces
  val collectorEndpoint = "http://otel-collector:4317"
  val tracer: Tracer = Tracing.getTracer(collectorEndpoint)

  // Candy!!
  val candyFactory : ActorSystem[CandyFactoryCommand] = ActorSystem(CandyFactoryActor(tracer), "CandyFactoryActor")
  candyFactory ! Start
}
