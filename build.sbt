name := "compositor"

version := "0.0.1"

versionScheme := Some("early-semver")

scalaVersion := "3.2.2"

enablePlugins(ScalaNativePlugin)

nativeLinkStubs := true

nativeMode := "debug"

nativeLinkingOptions := Seq(s"-L${baseDirectory.value}/native-lib")

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

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.15" % "test"

libraryDependencies ++= Seq(
  "io.github.edadma" %%% "libcairo" % "0.0.5",
  "io.github.edadma" %%% "freetype" % "0.0.2",
  "io.github.edadma" %%% "texish" % "0.0.8",
  "com.github.scopt" %%% "scopt" % "4.1.0",
  "com.lihaoyi" %%% "pprint" % "0.8.1",
)

publishMavenStyle := true

Test / publishArtifact := false
