name := "tagless-final-with-eff"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.atnos"      %% "eff"             % "5.5.2",
  "org.atnos"      %% "eff-cats-effect" % "5.5.2",
  "org.atnos"      %% "eff-doobie"      % "5.5.2",
  "org.typelevel"  %% "cats-core"       % "2.0.0",
  "org.typelevel"  %% "cats-effect"     % "2.0.0",
  "org.tpolecat"   %% "doobie-core"     % "0.8.0-RC1",
  "com.h2database" % "h2"               % "1.4.200"
)

addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
)
