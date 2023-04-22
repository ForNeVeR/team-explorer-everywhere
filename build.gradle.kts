import de.undercouch.gradle.tasks.download.Download
import java.nio.file.Files
import java.security.MessageDigest

plugins {
    id("de.undercouch.download") version "5.4.0"
}

ant.lifecycleLogLevel = AntBuilder.AntMessagePriority.INFO
ant.properties["dir.machine.build-runtime"] = file(".build/eclipse/eclipse").path
ant.importBuild("build/build.xml")

val downloadsCacheDir: String by extra
val eclipseDir: String by extra

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
tasks.register<Copy>("prepare-eclipse") {
    dependsOn("download-eclipse")
    dependsOn("download-egit")
    doFirst {
        verifyDownloadedFile(eclipseDownloadPath, eclipseSha256)
        verifyDownloadedFile(eGitDownloadPath, eGitSha256)
    }
    from(zipTree(eclipseDownloadPath)).into(eclipseDir)
    from(zipTree(eGitDownloadPath)).into(file(eclipseDir).resolve("eclipse"))
}

tasks.named("build_4_antrunner") {
    dependsOn("prepare-eclipse")
}

tasks.register("build") {
    dependsOn("ant-build")
}

tasks.wrapper {
    gradleVersion = "8.1.1"
    distributionType = Wrapper.DistributionType.ALL
}
