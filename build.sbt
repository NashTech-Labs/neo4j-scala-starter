name := """neo-with-scala"""

version := "1.0"

val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6"
val neo4j_driver = "org.neo4j.driver" % "neo4j-java-driver" % "1.0.4"

lazy val commonSettings = Seq(
  organization := "com.knoldus.neo4j",
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "neo4j_procedure",
    libraryDependencies ++= Seq(scalaTest, neo4j_driver)
  )
