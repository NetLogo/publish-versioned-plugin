package org.nlogo.build

import scala.sys.process.Process
import sbt._, Keys.{ `package` => packageKey, _ }

import sbt.complete.{ Parser, DefaultParsers }, Parser.success, DefaultParsers._

object PublishVersioned extends AutoPlugin {
  override def trigger = allRequirements
  override lazy val globalSettings = Seq(commands ++= Seq(packageVersioned, publishVersioned, publishLocalVersioned, packageVersionedCamel, publishVersionedCamel, publishLocalVersionedCamel))

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
      val sha      = Process("git rev-parse HEAD").lineStream.head take 7
      s"$version-$sha$dirtyStr"
    }

  private def projectParser(state: State): Parser[ProjectRef] = {
    val projects = Project.extract(state).structure.allProjectRefs
    if (projects.length == 1)
      success(projects.head)
    else
      (Space ~> {
        projects.map(projectRef => (projectRef.project ^^^ projectRef)).reduce(_ | _)
      }) !!! s"Supply a valid project name. Choices are: ${projects.map(_.project).mkString(", ")}"
  }

  private def commandHelp[T](cmdName: String, taskKey: Def.ScopedKey[Task[T]]): Help =
    Help(cmdName, (cmdName, "<subproject>"),
      s"""|$cmdName <subproject>
          |
          |  Runs ${taskKey.key.label} in the given subproject.
          |  If the project is a snapshot, it will supply a version from git.
          |  Otherwise, the build-specified version will be used for the project.
          |
          |  <subproject> may be omitted when there is only one project""".stripMargin)

  private def publishHelper[T](cmdName: String, taskKey: TaskKey[T]) = Command(
    cmdName, commandHelp(cmdName, taskKey))(projectParser) {
    (state, subproject) =>
      val extracted  = Project.extract(state).copy(currentRef = subproject)(Project.extract(state).showKey)
      val isSnap     = extracted.getOpt(isSnapshot).get.asInstanceOf[Boolean]
      val rawVersion = extracted.getOpt(version).get.asInstanceOf[String]
      val evaluationState = extracted.appendWithoutSession(
        List(version := genVersion(isSnap, rawVersion)), state)
      Project.runTask(
        taskKey in subproject,
        extracted.appendWithoutSession(List(version := genVersion(isSnap, rawVersion)), state),
        true
      )
      state
  }
}
