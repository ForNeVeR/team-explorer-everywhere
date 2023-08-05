import de.undercouch.gradle.tasks.download.Download
import java.nio.file.Files
import java.security.MessageDigest

plugins {
    id("de.undercouch.download") version "5.4.0"
}

val tfsVersion: String by extra
val productVersion = tfsVersion.split(".").run { Triple(this[0], this[1], this[2]) }

val downloadsCacheDir: String by extra
val eclipseBaseDir: String by extra
val eclipseDir = file(eclipseBaseDir).resolve("eclipse")

fun verifyDownloadedFile(file: File, sha256: String) {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = Files.readAllBytes(file.toPath())
    val hash = String.format("%064x", BigInteger(1, digest.digest(bytes)))
    if (!hash.equals(sha256, ignoreCase = true)) {
        delete(eclipseDownloadPath)
        error("Expected hash for file $file: $sha256, actual hash: $hash\nPlease retry build")
    }
}

val eGitUrl: String by extra
val eGitSha256: String by extra
val eGitDownloadPath = file(downloadsCacheDir).resolve("egit.zip")
tasks.register<Download>("download-egit") {
    src(eGitUrl)
    dest(eGitDownloadPath)
    overwrite(false)
}

val eclipseUrl: String by extra
val eclipseSha256: String by extra
val eclipseDownloadPath = file(downloadsCacheDir).resolve("eclipse.zip")
tasks.register<Download>("download-eclipse") {
    src(eclipseUrl)
    dest(eclipseDownloadPath)
    overwrite(false)
}
tasks.register("prepare-eclipse") {
    dependsOn("download-eclipse")
    dependsOn("download-egit")
    doLast {
        verifyDownloadedFile(eclipseDownloadPath, eclipseSha256)
        verifyDownloadedFile(eGitDownloadPath, eGitSha256)

        copy {
            from(zipTree(eclipseDownloadPath))
            into(eclipseBaseDir)
        }
        copy {
            from(zipTree(eGitDownloadPath))
            into(eclipseDir)
        }
    }
}

ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO
ant.properties["dir.machine.build-runtime"] = file(eclipseDir).path
ant.properties["number.version.major"] = productVersion.first
ant.properties["number.version.minor"] = productVersion.second
ant.properties["number.version.service"] = productVersion.third
ant.importBuild("build/build.xml")

tasks.named("build_4_antrunner") {
    dependsOn("prepare-eclipse")
}

val antBuild: TaskProvider<Task> = tasks.named("ant-build")
tasks.register("jar") {
    dependsOn(antBuild)
}

tasks.register("build") {
    dependsOn(antBuild)
}

tasks.wrapper {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.ALL
}
