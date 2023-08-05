// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.txt in the project root.

package com.microsoft.tfs

import com.microsoft.tfs.tests.TfsClientTestFixture
import com.microsoft.tfs.tests.cloneTestRepository
import com.microsoft.tfs.tests.deleteWorkspace
import kotlin.test.Test
import kotlin.test.assertFalse

class ServerWorkspaceClientTests : TfsClientTestFixture() {

    @Test
    fun clonedRepositoryHasReadOnlyFile() {
        val workspacePath = cloneTestRepository(clientExecutable, true)
        try {
            val filePath = workspacePath.resolve("readme.txt")
            assertFalse(filePath.toFile().canWrite())
        } finally {
            deleteWorkspace(clientExecutable, workspacePath)
        }
    }
}