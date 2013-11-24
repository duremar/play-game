name := "play-game"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "net.sf.barcode4j" % "barcode4j" % "2.0"
)     

play.Project.playScalaSettings
