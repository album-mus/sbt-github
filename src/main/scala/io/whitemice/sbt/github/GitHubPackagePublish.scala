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
    lazy val githubWhoAmI = taskKey[String]("The current settings")
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

  override lazy val buildSettings = Seq(
    githubUser := {
      val user = Try(s"git config github.actor" !!).map(_.trim)
      user match {
        case Success(value) => value
        case _ =>
          sys.env.get("GITHUB_ACTOR") match {
            case Some(value) => value
            case _ => "NO-USER-FOUND"
          }
      }
    },

    githubToken := {
      val token = Try(s"git config github.packageToken" !!).map(_.trim)
      token match {
        case Success(value) => value
        case _ =>
          sys.env.get("GITHUB_TOKEN") match {
            case Some(value) => value
            case _ => "NO-TOKEN-FOUND"
          }
      }
    },

    githubWhoAmI := {
      val log = streams.value.log
      val result = s"${githubRepoOwner.value}/${githubRepo.value}"
      log.info(s"github repo : ${githubRepoOwner.value}/${githubRepo.value}")
      log.info(s"github user : ${githubUser.value}")
      log.info(s"github token: ${githubToken.value}")
      result
    },

    githubRepo := "<REPO>",
    githubRepoOwner := "<OWNER>",

    publishTo := Some("GitHub Package Registry" at
      s"https://maven.pkg.github.com/${githubRepoOwner.value}/${githubRepo.value}"),

    credentials += Credentials("GitHub Package Registry",
      "maven.pkg.github.com",
      githubUser.value,
      githubToken.value),
  )
}
