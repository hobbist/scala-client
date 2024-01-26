import Dependencies.{commonDeps,fsDeps,monixDeps}

import scala.language.postfixOps

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.0"

logLevel := Level.Debug

lazy val domains =
    (project in file("Domains"))
    .settings(name:="domains", libraryDependencies++=commonDeps)

lazy val fs =
    (project in file("FS"))
    .dependsOn(domains)
    .settings(name:="fs", libraryDependencies++=commonDeps++fsDeps)    

lazy val reactive =
    (project in file("Reactive"))
    .dependsOn(domains)
    .settings(name:="reactive", libraryDependencies++=commonDeps++monixDeps)    

lazy val root =
    (project in file("."))
    .aggregate(domains,fs,reactive)
    .settings(name:="scala-client")

//val repoUser=sys.env.get("MAVEN_USER").getOrElse("")
//val repoPass=sys.env.get("MAVEN_SECRET").getOrElse("")

scalacOptions+="-Ypartial-unification"

//credentials+=Credentials("organization","url",repoUser,repoPass)

//resolvers+= Resolver.mavenLocal
//resolvers+= Resolver.url("organization",new URL(""))(Patterns("[organization]/[module]/[module]-[revision].[ext]"))
//resolvers+= Resolver.url("organization",new URL("")).withAllowInsecureProtocol(true)