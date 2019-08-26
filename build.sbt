import Dependencies._

lazy val appVersion = {
  val now = java.time.LocalDateTime.now()
  val timestamp = f"${now.getYear}%02d${1+now.getMonth.ordinal}%02d${now.getDayOfMonth}%02d_${now.getHour}%02d${now.getMinute}%02d"
  val v = s"1.1.0-${timestamp}"
  println(s"setting version to ${v}")
  v
}

scalaVersion in Global := "2.12.6"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      sbtPlugin := true,
      organization := "a8",
      version      := appVersion
    )),
    credentials += Credentials(Path.userHome / ".sbt" / "credentials"),
    publishTo := Some("Artifactory Realm" at "https://accur8.jfrog.io/accur8/sbt-plugins/"),
    name := "sbt-a8",
    libraryDependencies += scalaTest % Test
  )
