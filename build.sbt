name := "lunatech-traces-metrics-exemplars"

organization := "com.lunatech"

version := "0.0.1"

fork := true

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  Seq(
    "com.typesafe.akka"          %% "akka-actor-typed" % "2.6.18",
    "ch.qos.logback"             %  "logback-classic"  % "1.2.10",
    "com.typesafe.scala-logging" %% "scala-logging"    % "3.9.4"
  ),
  prometheus,
  openTelemetry
)
  .flatten

val prometheus = {
  val provider = "io.prometheus"
  val version  = "0.12.0"
  Seq(
    provider % "simpleclient"            % version,
    provider % "simpleclient_logback"    % version,
    provider % "simpleclient_hotspot"    % version,
    provider % "simpleclient_httpserver" % version
  )
}

val openTelemetry = {
  val provider = "io.opentelemetry"
  val version  = "1.9.1"
  Seq(
    provider % "opentelemetry-api"           % version,
    provider % "opentelemetry-sdk"           % version,
    provider % "opentelemetry-exporter-otlp" % version
  )
}

// Define a Dockerfile

enablePlugins(DockerPlugin)

docker / dockerfile := {
  val jarFile = (Compile / packageBin / sbt.Keys.`package`).value
  val classpath = (Compile / managedClasspath).value
  val mainclass = (Compile / packageBin / mainClass).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  // Make a colon separated classpath with the JAR file
  val classpathString = classpath.files.map("/app/" + _.getName).mkString(":") + ":" + jarTarget
  new Dockerfile {
    // Base image
    from("openjdk:17")
    // Add all files on the classpath
    add(classpath.files, "/app/")
    // Add the JAR file
    add(jarFile, jarTarget)
    // On launch run Java with the classpath and the main class
    entryPoint("java", "-cp", classpathString, mainclass)
  }
}

// Set names for the image
docker / imageNames := Seq(
  ImageName(namespace = Some(organization.value),
    repository = name.value,
    tag = Some("v" + version.value))
)


