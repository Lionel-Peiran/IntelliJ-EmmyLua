

package com.tang.intellij.test.completion

class TestTuple : TestCompletionBase() {

    fun `test tuple 1`() {
        doTest("""
            --- test_tuple_1.lua

            ---@class Type1
            local obj = { name = "name" }

            ---@return number, Type1
            local function getTuple()
            end

            local a, b = getTuple()
            b.--[[caret]]
        """) {
            assertTrue("name" in it)
        }
    }
}