sbtPlugin := true

scalaVersion := "2.12.4"

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "2.2"

isSnapshot := true

licenses += ("Public Domain", url("http://creativecommons.org/licenses/publicdomain/"))

publishTo := { Some("Cloudsmith API" at "https://maven.cloudsmith.io/netlogo/publish-versioned/") }

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

pomIncludeRepository := { x => false }
