

package com.tang.intellij.test.completion

class TestArgHistory : TestCompletionBase() {

    fun `test arg history 1`() {
        doTest("""
            --- test_arg_history_1.lua

            local function test(strArg1, strArg2)
            end

            test("his1")
            test("his2")
            test("--[[caret]]")
        """) {
            assertTrue(it.containsAll(listOf("his1", "his2")))
        }
    }

}