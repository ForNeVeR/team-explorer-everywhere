// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.txt in the project root.

package com.microsoft.tfs.tests

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun executeClient(clientExecutable: Path, directory: Path, vararg arguments: String) {
    println("Running tf with arguments: [${arguments.joinToString(",")}]")
    val loginArgument = "-login:${IntegrationTestUtils.user},${IntegrationTestUtils.pass}"
    val process = ProcessBuilder(clientExecutable.toString(), loginArgument, *arguments)
        .directory(directory.toFile())
        .apply {
            environment().also {
                it["TF_ADDITIONAL_JAVA_ARGS"] = "-Duser.country=US -Duser.language=en"
                it["TF_USE_KEYCHAIN"] = "FALSE" // will be stuck on com.microsoft.tfs.jni.internal.keychain.NativeKeychain.nativeFindInternetPassword on macOS otherwise
            }
        }
        .inheritIO()
        .start()
    val exitCode = process.waitFor()
    assertEquals(0, exitCode)
}

private fun tfCreateWorkspace(clientExecutable: Path, workspacePath: Path, name: String, isServer: Boolean) {
    val collectionUrl = IntegrationTestUtils.serverUrl
    val location = if (isServer) "server" else "local"
    executeClient(clientExecutable, workspacePath, "workspace", "-new", "-collection:$collectionUrl", "-location:$location", name)
}

private fun tfDeleteWorkspace(clientExecutable: Path, workspacePath: Path, workspaceName: String) {
    executeClient(clientExecutable, workspacePath, "workspace", "-delete", workspaceName)
}

private fun tfCreateMapping(clientExecutable: Path, workspacePath: Path, workspaceName: String, serverPath: String, localPath: Path) {
    executeClient(clientExecutable, workspacePath, "workfold", "-map", "-workspace:$workspaceName", serverPath, localPath.toString())
}

private fun tfGet(clientExecutable: Path, workspacePath: Path) {
    executeClient(clientExecutable, workspacePath, "get")
}

fun cloneTestRepository(clientExecutable: Path, isServer: Boolean = false): Path {
    val workspacePath = Files.createTempDirectory("adi.b.test.").toFile().canonicalFile.toPath()
    val workspaceName = "${workspacePath.fileName}.${IntegrationTestUtils.workspaceNameSuffix}"
    assertTrue(workspaceName.length <= 64)
    tfCreateWorkspace(clientExecutable, workspacePath, workspaceName, isServer)
    tfCreateMapping(clientExecutable, workspacePath, workspaceName, "$/${IntegrationTestUtils.teamProject}", Paths.get("."))
    tfGet(clientExecutable, workspacePath)
    return workspacePath
}

fun deleteWorkspace(clientExecutable: Path, path: Path) {
    val workspaceName = "${path.fileName}.${IntegrationTestUtils.workspaceNameSuffix}"
    tfDeleteWorkspace(clientExecutable, path, workspaceName)
}
