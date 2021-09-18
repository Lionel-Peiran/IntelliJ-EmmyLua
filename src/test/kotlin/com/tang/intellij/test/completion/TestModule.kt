

package com.tang.intellij.test.completion

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.tang.intellij.test.fileTreeFromText

class TestModule : TestCompletionBase() {

    fun `test module type`() {
        fileTreeFromText("""
             --- moduleA.lua
             ---@module TypeA
             module("TypeA")

             name = "a"

             --- B.lua
             local a ---@type TypeA
             a.--[[caret]]
        """).createAndOpenFileWithCaretMarker()

        FileDocumentManager.getInstance().saveAllDocuments()
        myFixture.completeBasic()
        val elementStrings = myFixture.lookupElementStrings
        assertTrue(elementStrings!!.contains("name"))
    }

    fun `test module field completion`() {
        fileTreeFromText("""
             --- moduleA.lua
             ---@module TypeA
             module("TypeA")

             name = "a"

             --[[caret]]
        """).createAndOpenFileWithCaretMarker()

        FileDocumentManager.getInstance().saveAllDocuments()
        myFixture.completeBasic()
        val elementStrings = myFixture.lookupElementStrings
        assertTrue(elementStrings!!.contains("name"))
    }

    fun `test module members visibility`() {
        fileTreeFromText("""
             --- moduleA.lua
             ---@module TypeA
             module("TypeA")

             myFieldName = "a"

             function myFunction() end

             --- B.lua
             --[[caret]]
        """).createAndOpenFileWithCaretMarker()

        FileDocumentManager.getInstance().saveAllDocuments()
        myFixture.completeBasic()
        val elementStrings = myFixture.lookupElementStrings
        assertFalse(elementStrings!!.containsAll(listOf("myFieldName", "myFunction")))
    }
}