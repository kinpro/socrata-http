import sbt._
import Keys._

import Dependencies._

object SocrataHttpCuratorBroker {
  val settings: Seq[Setting[_]] = BuildSettings.projectSettings ++ Seq(
    libraryDependencies ++= Seq(
      curatorDiscovery
    )
  )
}
