lazy val root = (project in file("."))
    .settings(
        publishArtifact := false
    )
  .aggregate(core, examples)
  .dependsOn(core, examples)

lazy val core = project.settings(
    libraryDependencies ++= Seq(
        "xmlunit" % "xmlunit" % "1.6",
        "org.codehaus.woodstox" % "woodstox-core-asl" % "4.4.1",
        "org.compass-project" % "compass" % "2.2.0",
        "org.scalatest" %% "scalatest" % "3.0.4" % Test,
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
    ),
    name := "xs4s"
)

def download(url: URL, to: File) = {
    import sbt.io._
    import sbt.io.IO._
    Using.urlInputStream(url) { inputStream =>
        transfer(inputStream, to)
    }
}

lazy val examples = project.dependsOn(core).settings(
    run := ((run in Runtime) dependsOn(downloadXmark)).evaluated,
    runMain := ((runMain in Runtime) dependsOn(downloadXmark)).evaluated,
    downloadXmark := {
        if (!file("downloads/xmark4.xml").exists()) {
            download(url("https://github.com/Saxonica/XT-Speedo/blob/master/data/xmark-tests/xmark4.xml?raw=true"), file("downloads/xmark4.xml"))
        }
    }
)


lazy val downloadXmark = taskKey[Unit]("Download speedo")
