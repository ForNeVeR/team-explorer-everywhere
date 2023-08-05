// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.txt in the project root.

package com.microsoft.tfs

import com.microsoft.tfs.tests.TfsClientTestFixture
import com.microsoft.tfs.tests.cloneTestRepository
import com.microsoft.tfs.tests.deleteWorkspace
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class LocalWorkspaceClientTests : TfsClientTestFixture() {

    @Test
    fun clonedRepositoryHasWriteableFile() {
        val workspacePath = cloneTestRepository(clientExecutable)
        try {
            val filePath = workspacePath.resolve("readme.txt")
            assertTrue(filePath.toFile().canWrite())
        } finally {
            deleteWorkspace(clientExecutable, workspacePath)
        }
    }
}
