// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.txt in the project root.

package com.microsoft.tfs.tests

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission
import java.util.zip.ZipFile
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.isExecutable

abstract class TfsClientTestFixture {

    private lateinit var clientExecutableDir: Path
    lateinit var clientExecutable: Path

    @BeforeEach
    fun setUp() {
        IntegrationTestUtils.ensureInitialized()
        val (dir, exe) = extractClientExecutable()
        clientExecutableDir = dir
        clientExecutable = exe
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun tearDown() {
        clientExecutableDir.deleteRecursively()
    }
}

fun extractClientExecutable(): Pair<Path, Path> {
    val archive = IntegrationTestUtils.clientZipArchivePath
    val destination = Files.createTempDirectory("tee-test-client")

    ZipFile(archive.toFile()).use { zip ->
        for (entry in zip.entries()) {
            val outputPath = destination.resolve(entry.name)
            if (entry.isDirectory) {
                Files.createDirectories(outputPath)
                continue
            }

            Files.newOutputStream(outputPath).use {
                zip.getInputStream(entry).copyTo(it)
            }
        }
    }

    val isWindows = System.getProperty("os.name").contains("win", ignoreCase = true)
    val clientDirBase = destination.resolve("TEE-CLC-${IntegrationTestUtils.clientVersion}")
    val clientExecutableFile = if (isWindows) {
        clientDirBase.resolve("tf.cmd")
    } else {
        clientDirBase.resolve("tf")
    }
    if (!clientExecutableFile.exists()) {
        error("Could not find file \"${clientExecutableFile}\".")
    }
    if (!clientExecutableFile.isExecutable()) {
        Files.setPosixFilePermissions(clientExecutableFile, Files.getPosixFilePermissions(clientExecutableFile).apply {
            add(PosixFilePermission.OWNER_EXECUTE)
        })
    }
    return destination to clientExecutableFile
}
