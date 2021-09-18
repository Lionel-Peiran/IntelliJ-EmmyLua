

package com.tang.intellij.test

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language



abstract class LuaTestBase : BasePlatformTestCase() {
    protected fun checkByDirectory(@Language("Lua") before: String, @Language("Lua") after: String, action: () -> Unit) {
        fileTreeFromText(before).create()
        action()
        FileDocumentManager.getInstance().saveAllDocuments()
        fileTreeFromText(after).assertEquals(myFixture.findFileInTempDir(""))
    }

    private fun FileTree.create(): TestProject =
            create(myFixture.project, myFixture.findFileInTempDir(""))

    protected fun FileTree.createAndOpenFileWithCaretMarker(): TestProject {
        val testProject = create()
        myFixture.configureFromTempProjectFile(testProject.fileWithCaret)
        return testProject
    }
}