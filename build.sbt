
name := "sbt-github"
version := "0.1"
organization := "io.whitemice"
scalaVersion := "2.12.10"

sbtPlugin := true
sbtVersion := "1.3.3"
enablePlugins(SbtPlugin)

scriptedLaunchOpts ++= Seq("-Dplugin.version=" + version.value)
scriptedBufferLog := true

bintrayOrganization := Some("whitemice")
bintrayRepository := "maven"
