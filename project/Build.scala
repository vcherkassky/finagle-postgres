import sbt._
import Keys._


object FinaglePostgres extends Build {

  val baseSettings = Defaults.defaultSettings ++ Seq(resolvers += "twitter-repo" at "http://maven.twttr.com",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2" % "1.12.2" % "test",
      "junit" % "junit" % "4.7"	% "test",
      "com.twitter" % "finagle-core" % "5.3.0",
      "com.twitter" % "util-logging" % "5.3.6"
    ))

  lazy val buildSettings = Seq(
    organization := "com.github.mairbek",
    version := "0.0.2-SNAPSHOT",
    scalaVersion := "2.9.2"
  )

  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    publishArtifact := true,
    publishTo := Some(Resolver.file("localDirectory",  file(Path.userHome.absolutePath + "/Projects/personal/mvn-repo"))),
    licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/mairbek/finagle-postgres")),
    pomExtra := (
      <scm>
        <url>git://github.com/mairbek/finagle-postgres.git</url>
        <connection>scm:git://github.com/mairbek/finagle-postgres.git</connection>
      </scm>
      <developers>
        <developer>
          <id>mairbek</id>
          <name>Mairbek Khadikov</name>
          <url>http://github.com/mairbek</url>
        </developer>
    </developers>)
  )

  lazy val root = Project(id = "finagle-postgres",
    base = file("."),
    settings = baseSettings ++ buildSettings ++ publishSettings)

}
