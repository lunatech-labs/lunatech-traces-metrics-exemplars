package com.lunatech.exemplars.observability

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.`export`.BatchSpanProcessor
import io.opentelemetry.sdk.resources.{Resource => OtelResource}
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes

object Tracing {
  def getTracer(spansCollectorEndpoint: String): Tracer = {
    lazy val sdkTracerProvider: SdkTracerProvider = SdkTracerProvider
      .builder()
      .setResource(
        OtelResource.create(
          Attributes.of(ResourceAttributes.SERVICE_NAME, "Lunatech candy factory")
        )
      )
      .addSpanProcessor(
        BatchSpanProcessor
          .builder(OtlpGrpcSpanExporter.builder().setEndpoint(spansCollectorEndpoint).build())
          .build()
      )
      .build()

    lazy val openTelemetry: OpenTelemetrySdk = OpenTelemetrySdk
      .builder()
      .setTracerProvider(sdkTracerProvider)
      .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
      .buildAndRegisterGlobal()

    openTelemetry.getTracer("lunatech-candy-factory")
  }
}
