

package com.tang.intellij.test.completion

class TestAlias : TestCompletionBase() {
    fun `test alias 1`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_alias.lua

            ---@alias Handler fun(emmy: Emmy):void

            ---@param func Handler
            local function test(func)
            end

            test(function(value)
                value.--[[caret]]
            end)
        """) {
            assertTrue("sayHello" in it)
        }
    }
}