name := "My Akka FSM Project"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka"   %%    "akka-actor"    %   "2.4.8",
  "com.typesafe.akka"   %%    "akka-testkit"  %   "2.4.8"   %   "test",
  "org.scalatest"       %%    "scalatest"     %   "2.2.5"   %   "test"
)

parallelExecution in Test := false // Auto calculated Internal Thread Pool Size = Available Processors x 2
