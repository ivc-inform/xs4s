lazy val root = (project in file("."))
  .aggregate(core, examples)
  .dependsOn(core, examples)

lazy val core = project.settings(
    libraryDependencies ++= Seq(
        "xmlunit" % "xmlunit" % "1.6",
        "org.codehaus.woodstox" % "woodstox-core-asl" % "4.4.1",
        "org.compass-project" % "compass" % "2.2.0",
        "org.scalatest" %% "scalatest" % "3.0.3" % Test,
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
    ),
    name := "xs4s",
    publishTo := {
        val corporateRepo = "http://toucan.simplesys.lan/"
        if (isSnapshot.value)
            Some("snapshots" at corporateRepo + "artifactory/libs-snapshot-local")
        else
            Some("releases" at corporateRepo + "artifactory/libs-release-local")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
)

lazy val examples = project.dependsOn(core).settings(
    run := ((run in Runtime) dependsOn(downloadCarparks, downloadXmark)).evaluated,
    runMain := ((runMain in Runtime) dependsOn(downloadCarparks, downloadXmark)).evaluated,
    downloadCarparks := {
        import sbt._
        import IO._
        if (!file("downloads/carparks-data").exists()) {
            download(url("http://81.17.70.199/carparks/data.zip"), file("downloads/carparks-data.zip"))
            unzip(file("downloads/carparks-data.zip"), file("downloads/carparks-data"))
        }
    },
    downloadXmark := {
        import sbt.IO._
        if (!file("downloads/xmark4.xml").exists()) {
            download(url("https://github.com/Saxonica/XT-Speedo/blob/master/data/xmark-tests/xmark4.xml?raw=true"), file("downloads/xmark4.xml"))
        }
    }
)

lazy val downloadCarparks = taskKey[Unit]("Download carparks task")

lazy val downloadXmark = taskKey[Unit]("Download speedo")
