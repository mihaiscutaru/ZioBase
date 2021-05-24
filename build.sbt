
val akkaHttpVersion = "10.2.1"
val akkaVersion = "2.6.10"
val refinedVersion = "0.9.18"
val zioVersion = "1.0.7"
val zioLoggingVersion = "0.5.8"
val zioConfigVersion = "1.0.4"

val root = (project in file("."))
  .settings(
    scalacOptions ++= Seq("-Ymacro-annotations"),
    inThisBuild(
      List(
        organization := "io.scalac",
        scalaVersion := "2.13.5",
        idePackagePrefix := Some("io.scalac.labs.iot")
      )
    ),
    name := "ZioBase",
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka"             %% "akka-http"                       % akkaHttpVersion,
      "de.heikoseeberger"             %% "akka-http-play-json"             % "1.35.2",
      "com.typesafe.akka"             %% "akka-actor-typed"                % akkaVersion,
      "com.typesafe.akka"             %% "akka-stream"                     % akkaVersion,
      "dev.zio"                       %% "zio"                             % zioVersion,
      "dev.zio"                       %% "zio-streams"                     % zioVersion,
      "dev.zio"                       %% "zio-logging"                     % zioLoggingVersion,
      "dev.zio"                       %% "zio-logging-slf4j"               % zioLoggingVersion,
      "dev.zio"                       %% "zio-config"                      % zioConfigVersion,
      "dev.zio"                       %% "zio-config-magnolia"             % zioConfigVersion,
      "dev.zio"                       %% "zio-config-refined"              % zioConfigVersion,
      "dev.zio"                       %% "zio-config-typesafe"             % zioConfigVersion,
      "dev.zio"                       %% "zio-interop-reactivestreams"     % "1.0.3.5",
      "io.scalac"                     %% "zio-akka-http-interop"           % "0.4.0",
      "ch.qos.logback"                % "logback-classic"                  % "1.2.3",
      "eu.timepit"                    %% "refined"                         % refinedVersion,
      "io.circe"                      %% "circe-derivation"                % "0.13.0-M4",
      "io.circe"                      %% "circe-generic"                   % "0.13.0",
      "io.circe"                      %% "circe-generic-extras"            % "0.13.0",
      "dev.zio"                       %% "zio-test"                        % zioVersion  % Test,
      "io.minio"                      % "minio"                            % "6.0.13"
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
  )