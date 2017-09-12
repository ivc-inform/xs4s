import sbt.Keys._
import sbt.{Credentials, Path, _}

object CommonSettingsPlugin extends AutoPlugin {
    override def trigger = allRequirements
    override def projectSettings = Seq(
        scalaVersion := "2.12.3",
        organization := "com.scalawilliam",
        version := "0.2.1-SNAPSHOT"
    ) ++ Seq(
        publishMavenStyle := true,
        publishArtifact in Test := false,
        publishTo := {
            val corporateRepo = "http://toucan.simplesys.lan/"
            if (isSnapshot.value)
                Some("snapshots" at corporateRepo + "artifactory/libs-snapshot-local")
            else
                Some("releases" at corporateRepo + "artifactory/libs-release-local")
        },
        credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
    )
}
