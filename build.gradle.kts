buildDir = file("gradle-build")

ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO
ant.properties["dir.machine.build-runtime"] = file(".build/eclipse/eclipse").path
ant.importBuild("build/build.xml")

tasks.wrapper {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.ALL
}
