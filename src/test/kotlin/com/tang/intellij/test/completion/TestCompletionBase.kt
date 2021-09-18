

package com.tang.intellij.test.completion

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.tang.intellij.lua.project.LuaSettings
import com.tang.intellij.test.LuaTestBase
import com.tang.intellij.test.fileTreeFromText

abstract class TestCompletionBase : LuaTestBase() {
    override fun getTestDataPath(): String {
        return "src/test/resources/completion"
    }

    fun doTestWithResult(result: String) {
        doTest {
            assertTrue(result in it)
        }
    }

    fun doTestWithResult(result: Collection<String>) {
        doTest {
            assertTrue(it.containsAll(result))
        }
    }

    fun doTest(action: (lookupStrings:List<String>) -> Unit) {
        LuaSettings.instance.isShowWordsInFile = false
        FileDocumentManager.getInstance().saveAllDocuments()
        myFixture.completeBasic()
        val strings = myFixture.lookupElementStrings
        assertNotNull(strings)
        action(strings!!)
    }

    fun doTest(code: String, action: (lookupStrings:List<String>) -> Unit) {
        fileTreeFromText(code).createAndOpenFileWithCaretMarker()
        doTest(action)
    }
}