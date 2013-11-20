sbtPlugin := true

name := "publish-versioned-plugin"

organization := "org.nlogo"

version := "0.9-BOOTSTRAP"

licenses += ("Public Domain", url("http://creativecommons.org/licenses/publicdomain/"))

bintrayPublishSettings

bintray.Keys.repository in bintray.Keys.bintray := "publish-versioned"

bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("netlogo")
