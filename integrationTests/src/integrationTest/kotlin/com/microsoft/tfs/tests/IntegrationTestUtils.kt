// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.txt in the project root.

package com.microsoft.tfs.tests

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.test.assertFalse
import kotlin.test.assertTrue

object IntegrationTestUtils {
    private const val ENV_WORKSPACE_SUFFIX = "TEE_WORKSPACE_SUFFIX"
    private const val ENV_SERVER_URL = "TEE_SERVER_URL"
    private const val ENV_TEAM_PROJECT = "TEE_TEAM_PROJECT"
    private const val ENV_USER = "TEE_USER"
    private const val ENV_PASSWORD = "TEE_PASS"

    val workspaceNameSuffix: String = System.getenv(ENV_WORKSPACE_SUFFIX) ?: "default"
    val serverUrl: String = System.getenv(ENV_SERVER_URL)
    val teamProject: String = System.getenv(ENV_TEAM_PROJECT)
    val user: String = System.getenv(ENV_USER)
    val pass: String = System.getenv(ENV_PASSWORD)
    val clientVersion: String = System.getProperty("tee.client.version")

    private val repositoryRoot: Path = run {
        val cwd = Path(System.getProperty("user.dir"))
        var currentDirectory: Path? = cwd
        while (currentDirectory != null && !currentDirectory.resolve(".repo.root.marker").exists()) {
            currentDirectory = currentDirectory.parent
        }

        if (currentDirectory == null)
            error("Could not fine the root repository directory from path \"$cwd\".")

        currentDirectory
    }
    val clientZipArchivePath = repositoryRoot.resolve("build/output/bin/clc/TEE-CLC-$clientVersion.zip")

    fun ensureInitialized() {
        val message = "You must provide %s for the integration tests through the following environment variable: %s"
        fun assertNotEmpty(name: String, env: String, value: String) {
            assertFalse(value.isEmpty(), String.format(message, name, env))
        }

        assertNotEmpty("serverUrl", ENV_SERVER_URL, serverUrl)
        assertNotEmpty("teamProject", ENV_TEAM_PROJECT, teamProject)
        assertNotEmpty("user", ENV_USER, user)
        assertNotEmpty("pass", ENV_PASSWORD, pass)
        assertFalse("Client version should be passed via \"tee.client.version\" system property.") {
            clientVersion.isEmpty()
        }
        assertTrue("File \"$clientZipArchivePath\" should exist.") { clientZipArchivePath.exists() }
    }
}
