name := """Fast Fluency"""
organization := "Computer Geeks, Inc."

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies += guice

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaWs,
  evolutions
)

libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-core" % "5.4.9.Final",
  "mysql" % "mysql-connector-java" % "8.0.11"
)
