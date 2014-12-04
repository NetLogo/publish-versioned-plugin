import sbt._, Keys.{ `package` => packageKey, _ }

object Plugin extends Plugin {

  object PublishVersioned {

    lazy val settings = Seq(commands ++= Seq(packageVersioned, publishVersioned, publishLocalVersioned, packageVersionedCamel, publishVersionedCamel, publishLocalVersionedCamel))

    private def packageVersioned      = publishHelper("package-versioned",       packageKey   in Compile)
    private def publishVersioned      = publishHelper("publish-versioned",       publish      in Compile)
    private def publishLocalVersioned = publishHelper("publish-local-versioned", publishLocal in Compile)

    private def packageVersionedCamel      = publishHelper("packageVersioned",      packageKey   in Compile)
    private def publishVersionedCamel      = publishHelper("publishVersioned",      publish      in Compile)
    private def publishLocalVersionedCamel = publishHelper("publishLocalVersioned", publishLocal in Compile)

    private def genVersion(isSnapshot: Boolean, version: String): String =
      if (!isSnapshot)
        version
      else {
        val isClean  = Process("git diff --quiet --exit-code HEAD").! == 0
        val dirtyStr = if (isClean) "" else "-dirty"
        val sha      = Process("git rev-parse HEAD").lines.head take 7
        s"$version-$sha$dirtyStr"
      }

    private def publishHelper[T](cmdName: String, taskKey: Def.ScopedKey[Task[T]]) = Command.command(cmdName) {
      state =>
        val extracted  = Project.extract(state)
        val isSnap     = extracted.getOpt(isSnapshot).get.asInstanceOf[Boolean]
        val rawVersion = extracted.getOpt(version).get.asInstanceOf[String]
        Project.runTask(
          taskKey,
          extracted.append(List(version := genVersion(isSnap, rawVersion)), state),
          true
        )
        state
    }

  }

}
