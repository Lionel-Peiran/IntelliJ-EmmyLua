

package com.tang.intellij.test.vararg

import com.tang.intellij.test.completion.TestCompletionBase

class VarargTest : TestCompletionBase() {
    fun `test vararg 1`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_vararg_1.lua
            
            ---@generic T
            ---@param index number|string
            ---@vararg T
            ---@return T
            local function select(index, ...)
            end

            ---@type Emmy
            local emmy = {}

            local r = select(1, emmy, emmy, emmy)
            r.--[[caret]]
        """) {
            assertTrue(it.contains("sayHello"))
        }
    }

    fun `test vararg 2`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_vararg_2.lua

            ---@vararg Emmy
            local function test(...)
                local t = {...}
                t[1].--[[caret]]
            end
        """) {
            assertTrue(it.contains("sayHello"))
        }
    }
}