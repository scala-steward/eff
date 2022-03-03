addSbtPlugin("com.github.sbt"       % "sbt-pgp"                       % "2.1.2")
addSbtPlugin("com.typesafe.sbt"     % "sbt-git"                       % "1.0.2")
addSbtPlugin("org.scala-js"         % "sbt-scalajs"                   % "1.9.0")
addSbtPlugin("org.portable-scala"   % "sbt-scalajs-crossproject"      % "1.1.0")
addSbtPlugin("org.portable-scala"   % "sbt-scala-native-crossproject" % "1.1.0")
addSbtPlugin("org.xerial.sbt"       % "sbt-sonatype"                  % "3.9.12")
addSbtPlugin("ohnosequences"        % "sbt-github-release"            % "0.7.0")
addSbtPlugin("com.eed3si9n"         % "sbt-buildinfo"                 % "0.11.0")
addSbtPlugin("org.scala-native"     % "sbt-scala-native"              % "0.4.4")

// https://github.com/ohnosequences/sbt-github-release/issues/28#issuecomment-426086656
libraryDependencies += "com.sun.activation" % "javax.activation" % "1.2.0"
