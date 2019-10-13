package a8.sbt_a8.dobby

import a8.sbt_a8.{ProjectLogger, SharedSettings}
import sbt.Keys.{baseDirectory, managedClasspath, streams}
import sbt.{Attributed, Def, taskKey}
import sbt._
import Keys._

import scala.collection.mutable

trait DobbySettings { self: SharedSettings =>

  val dobbyActiveServers = mutable.HashMap[Int, WebServer]()
  val dobbyHttpPort = 8000

  import DobbyImpl._

  lazy val dobbyStart = taskKey[Unit]("Start the Dobby web server")
  lazy val dobbySetup = taskKey[Unit]("Generate webapp-composite folder that the Dobby web server will use")
  lazy val dobbyStop = taskKey[Unit]("Stop the Dobby web server")

  def dobbySettings: Seq[Def.Setting[_]] = {
    Seq(
      dobbySetup := runSetup(baseDirectory.value, (managedClasspath in Compile).value, (managedClasspath in Test).value, true)(ProjectLogger(baseDirectory.value.name, streams.value.log)),
      dobbyStart := runStart(baseDirectory.value, (managedClasspath in Compile).value, (managedClasspath in Test).value, dobbyHttpPort, this)(ProjectLogger(baseDirectory.value.name, streams.value.log)),
      dobbyStop  := stopServer(dobbyHttpPort, this)(ProjectLogger(baseDirectory.value.name, streams.value.log)),
      (onUnload in Global) := {
        (onUnload in Global).value.compose { state =>
          dobbyActiveServers
            .get(dobbyHttpPort)
            .foreach(_.stop())
          state
        }
      },
    )
  }

}
