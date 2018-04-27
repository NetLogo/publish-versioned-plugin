resolvers += Resolver.bintrayIvyRepo("sbt", "sbt-plugin-releases")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

resolvers += Resolver.bintrayIvyRepo("netlogo", "publish-versioned")

addSbtPlugin("org.nlogo" % "publish-versioned-plugin" % "2.1")
