repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.9.0"
}

val integrationTest: SourceSet by sourceSets.creating
configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("commons-io:commons-io:2.13.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

val tfsVersion: String by extra

tasks.test {
    useJUnitPlatform()
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    dependsOn(":zip")

    description = "Runs integration tests."
    group = "verification"

    useJUnitPlatform()
    testLogging.showStandardStreams = true

    testClassesDirs = integrationTest.output.classesDirs
    classpath = configurations[integrationTest.runtimeClasspathConfigurationName] + integrationTest.output
    systemProperty("tee.client.version", tfsVersion)

    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(integrationTestTask)
}
