sbtPlugin := true

scalaVersion := "2.12.4"

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "2.2"

isSnapshot := true

licenses += ("Creative Commons Zero v1.0 Universal Public Domain Dedication", url("https://creativecommons.org/publicdomain/zero/1.0/"))

publishTo := { Some("Cloudsmith API" at "https://maven.cloudsmith.io/netlogo/publish-versioned/") }
