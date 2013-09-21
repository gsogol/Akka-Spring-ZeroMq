name := "akka-java-spring"

version := "0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.0",
  "org.springframework" % "spring-context" % "3.2.2.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.9" % "test->default"
)

libraryDependencies += "com.typesafe.akka" % "akka-zeromq_2.10" % "2.2.1"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")
