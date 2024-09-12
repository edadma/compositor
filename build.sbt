name := "compositor"

version := "0.0.1"

versionScheme := Some("early-semver")

scalaVersion := "3.5.0"

enablePlugins(ScalaNativePlugin)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:existentials",
)

organization := "io.github.edadma"

githubOwner := "edadma"

githubRepository := name.value

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers += Resolver.githubPackages("edadma")

licenses := Seq("ISC" -> url("https://opensource.org/licenses/ISC"))

homepage := Some(url("https://github.com/edadma/" + name.value))

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.19" % "test"

libraryDependencies ++= Seq(
  "io.github.edadma" %%% "libcairo" % "0.0.7",
  "io.github.edadma" %%% "freetype" % "0.0.3",
  "io.github.edadma" %%% "texish" % "0.0.9",
  "io.github.edadma" %%% "char-reader" % "0.1.12",
  "io.github.edadma" %%% "xml" % "0.0.7",
)

libraryDependencies ++= Seq(
  "com.github.scopt" %%% "scopt" % "4.1.0",
  "com.lihaoyi" %%% "pprint" % "0.9.0",
)

publishMavenStyle := true

Test / publishArtifact := false
