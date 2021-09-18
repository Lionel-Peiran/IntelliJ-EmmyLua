

package com.tang.intellij.test.completion

class TestStringLiteralType : TestCompletionBase() {
    fun `test literal type 1`() {
        doTest("""
            --- test_tuple_1.lua

            ---@param type 'A' | "B"
            local function addListener(type, func)
            end

            addListener(--[[caret]]
        """) {
            assertTrue(it.containsAll(listOf("A", "B")))
        }
    }

    fun `test issue #338`() {
        doTest("""
            --- test_issue_338.lua

            ---@alias Al '"qwe"' | '"asd"'

            ---@class Qwe
            ---@field a fun(self:self, args:Al):any
            
            ---@type Qwe
            local zxc
            
            zxc:a(--[[caret]])
        """) {
            assertTrue(it.containsAll(listOf("\"qwe\"", "\"asd\"")))
        }
    }
}