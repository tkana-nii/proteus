import scala.io.Source

name := "proteus"

organization := "jp.ac.nii"

description := "Scala driver for ArangoDB"

version := Source.fromFile("./.version").getLines().toList.head

scalaVersion := "2.12.6"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

scalacOptions ++= Seq("-feature")

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

//sonatypeProfileName := "jp.ac.nii"
organization := "jp.ac.nii"

// useGpg := false

publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
   else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra in Global := {
   <url>http://www.cornfluence.com</url>
      <scm>
         <url>git@github.com:tkana-nii/proteus.git</url>
         <connection>scm:git@github.com:tkana-nii/proteus.git</connection>
      </scm>
      <developers>
         <developer>
            <id>nii-tkana</id>
            <name>T. Kanazawa</name>
         </developer>
         <developer>
            <id>CharlesAHunt</id>
            <name>Charles A Hunt</name>
            <url>http://www.cornfluence.com</url>
         </developer>
      </developers>
  }

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

parallelExecution in Test := false

resolvers ++= Seq(
  "OSS"  at "http://oss.sonatype.org/content/repositories/releases"
)

shellPrompt := { state => scala.Console.YELLOW + "[" + scala.Console.CYAN + Project.extract(state).currentProject.id + scala.Console.YELLOW + "]" + scala.Console.RED + " $ " + scala.Console.RESET }

libraryDependencies ++= {
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "ch.qos.logback" % "logback-core" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.typesafe" % "config" % "1.3.1"
  )
}

//val circeVersion = "0.6.1"
val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

