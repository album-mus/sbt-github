# sbt-github
Plugin for publishing and consuming artifacts hosted on [GitHub Package Registry](https://help.github.com/en/articles/about-github-package-registry).

## Publish or Consume
This `sbt` plugin is for both publishing:

```scala
githubRepOwner := "my-account"
githubRepo := "my-project"
sbt publish
```

and consuming artifacts:

```scala
resolvers += Resolver.githubPackages("my-account", "my-project")
libraryDependencies += "org.example" %% "my-project" % "3.0.8" % "test"
```

## Install

Add the following plugin hosted on [BinTray](https://bintray.com) to sbt:

```scala
resolvers += Resolver.bintrayIvyRepo("whitemice", "maven")
addSbtPlugin("io.whitemice" %% "sbt-github" % "0.3")
```

## Configuration

The plugin needs your github username and an access token with repo privileges in order to function.
Both can be specified in either your git configuration (`~/.gitconfig`):
   
```
[github]
        actor = <github-username>
        packageToken = <github access token with repo privileges>
```

or, as environment variables:

```
export GITHUB_ACTOR=<github-username>
export GITHUB_PACKAGE_TOKEN=<github access token with repo privileges>
```

## Usage

### To Publish

```scala
githubRepOwner := "my-account"
githubRepo := "my-project"
sbt publish
```

### To Consume

```scala
resolvers += Resolver.githubPackages("my-account", "my-project")
libraryDependencies += "org.example" %% "my-project" % "3.0.8" % "test"
```

### To peek under the hood
```
> githubWhoAmI
[info] github repo : my-account/my-project
[info] github user : <username>
[info] github token: <token>
```

## Plugin API
### Settings

```
githubRepoOwner := my-account`
githubRepo := my-project`
```

### Tasks

```
> githubWhoAmI
> githubUser
> githubToken
````
