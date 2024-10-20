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
      "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion
    )
  )
