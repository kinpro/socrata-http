import sbt._
import Keys._

import Dependencies._

object SocrataHttpCommon {
  val settings: Seq[Setting[_]] = BuildSettings.projectSettings ++ Seq(
    libraryDependencies ++= Seq(
      commonsLang,
      slf4jApi,
      simpleArm,
      scalaCheck % "test"
    )
  )
}