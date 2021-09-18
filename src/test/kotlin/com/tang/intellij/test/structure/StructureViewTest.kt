

package com.tang.intellij.test.structure

import com.intellij.testFramework.PlatformTestUtil
import com.intellij.util.ui.tree.TreeUtil
import com.tang.intellij.test.LuaTestBase
import org.intellij.lang.annotations.Language

class StructureViewTest : LuaTestBase() {

    fun testStructureViewBase() {
        doTest("""
             ---@class A
             local m = {}

             function m:method()end
        """.trimIndent(), """
            -main.lua
             -A
              method()
        """.trimIndent())
    }

    private fun doTest(@Language("lua") code: String, expected: String) {
        val normExpected = expected.trimIndent() + "\n"
        myFixture.configureByText("main.lua", code)
        myFixture.testStructureView {
            TreeUtil.expandAll(it.tree)
            PlatformTestUtil.assertTreeEqual(it.tree, normExpected)
        }
    }
}