sbtPlugin := true

scalaVersion := "2.12.4"

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "2.2"

isSnapshot := true

licenses += ("Public Domain", url("http://creativecommons.org/licenses/publicdomain/"))

bintrayRepository := "publish-versioned"

bintrayOrganization := Some("netlogo")
