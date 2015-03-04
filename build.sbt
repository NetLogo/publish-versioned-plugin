sbtPlugin := true

scalaVersion := "2.10.5"

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "2.0"

isSnapshot := true

licenses += ("Public Domain", url("http://creativecommons.org/licenses/publicdomain/"))

seq(bintrayPublishSettings: _*)

seq(PublishVersioned.settings: _*)

bintray.Keys.repository in bintray.Keys.bintray := "publish-versioned"

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("netlogo")
