addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.3.0")

resolvers += ("Nykredit Central proxy" at "https://maven.nykreditnet.net/nexus/repository/central/")
resolvers ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeIvyRepo("releases"),
  ("Artima Maven Repository" at "http://dl.bintray.com/sbt/sbt-plugin-releases").withAllowInsecureProtocol(true),
) ++ Resolver.sonatypeOssRepos("public")