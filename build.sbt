sbtPlugin := true

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "1.1"

isSnapshot := true

licenses += ("Public Domain", url("http://creativecommons.org/licenses/publicdomain/"))

seq(bintrayPublishSettings: _*)

seq(PublishVersioned.settings: _*)

bintray.Keys.repository in bintray.Keys.bintray := "publish-versioned"

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("netlogo")
