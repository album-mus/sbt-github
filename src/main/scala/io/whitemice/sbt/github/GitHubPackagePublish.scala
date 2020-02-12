package io.whitemice.sbt.github

import sbt._
import Keys._

import scala.util.{Success, Try}
import scala.sys.process._
import sbt.Keys.streams

import scala.language.postfixOps

object GitHubPackagePublish extends AutoPlugin {
  override def trigger = allRequirements
  override def requires: Plugins = plugins.JvmPlugin

  object autoImport {
    lazy val githubUser = taskKey[String]("The user's github username (e.g. melton1968)")
    lazy val githubToken = taskKey[String]("A github access token")
    lazy val githubRepo = settingKey[String]("The target repo (e.g. scala-time)")
    lazy val githubRepoOwner = settingKey[String]("The target repo's owner (e.g. album-mus)")

    // TODO You have to use ThisBuild / Resolver.githubPackages(...)
    // -- Is there a way to return something that is in the global scope?
    implicit class GHResolver(val resolver: Resolver.type) extends AnyVal {
      def githubPackages(owner: String, repo: String): MavenRepository =
        s"GitHub Package Registry ($owner/$repo)" at s"https://maven.pkg.github.com/$owner/$repo"
    }
  }

  import autoImport._

  override lazy val globalSettings = Seq(
    githubUser := {
      val log = streams.value.log
      val user = Try(s"git config github.actor" !!).map(_.trim)
      user match {
        case Success(value) => value
        case _ =>
          val user = sys.env("GITHUB_ACTOR")
          log.info(s"github user set to $user")
          user
      }
    },

    githubToken := {
      val log = streams.value.log
      val token = Try(s"git config github.packageToken" !!).map(_.trim)
      token match {
        case Success(value) => value
        case _ =>
          val token = sys.env("GITHUB_TOKEN")
          log.info(s"github access token set to $token")
          token
      }
    },

    githubRepo := "github-repo name goes here (e.g. scala-time)",
    githubRepoOwner := "github-repo owner goes here (e.g. album-mus)",

    publishTo := Some("GitHub Package Registry" at
      s"https://maven.pkg.github.com/${githubRepoOwner.value}/${githubRepo.value}"),

    credentials += Credentials("GitHub Package Registry",
      "maven.pkg.github.com",
      githubUser.value,
      githubToken.value),
  )
}
