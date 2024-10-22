ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

val PekkoVersion = "1.1.1"
val PekkoHttpVersion = "1.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "com.eutonies.claptrap.server",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % PekkoVersion,
      "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
      "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-http-cors" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-http-spray-json" % PekkoHttpVersion,
      "org.bouncycastle" % "bcpkix-jdk18on" % "1.78.1",
      "com.github.swagger-akka-http" %% "swagger-pekko-http" % "2.12.0",
      "com.github.swagger-akka-http" %% "swagger-enumeratum-module" % "2.10.0",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.18.0",
      "jakarta.ws.rs" % "jakarta.ws.rs-api" % "3.0.0",
      "com.typesafe.slick" %% "slick" % "3.5.1"
    )
  )
