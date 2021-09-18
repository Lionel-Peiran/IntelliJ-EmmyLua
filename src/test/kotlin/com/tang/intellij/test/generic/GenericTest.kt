

package com.tang.intellij.test.generic

import com.tang.intellij.test.completion.TestCompletionBase

class GenericTest : TestCompletionBase() {

    fun `test generic 1`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_generic.lua

            ---@generic T
            ---@param p1 T
            ---@return T
            local function test(p1)
                return p1
            end

            local value = test(emmy)
            value.--[[caret]]
        """) {
            assertTrue(it.contains("sayHello"))
        }
    }

    fun `test generic 2`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_generic.lua

            ---@generic T
            ---@param p1 T
            ---@param func fun(value: T):void
            local function test(p1, func)
            end

            test(emmy, function(value)
                value.--[[caret]]
            end)
        """) {
            assertTrue(it.contains("sayHello"))
        }
    }

    fun `test generic 3`() {
        myFixture.configureByFile("class.lua")
        doTest("""
            --- test_generic.lua

            ---@class EmmyExt : Emmy
            local ext = {}

            function ext:reading() end

            ---@generic T
            ---@param p1 T
            ---@param func fun(value: T):void
            local function test(p1, func)
            end

            test(ext, function(value)
                value.--[[caret]]
            end)
        """) {
            assertTrue(it.contains("reading"))
        }
    }

//    fun `test custom iterator`() {
//        myFixture.configureByFile("class.lua")
//        doTest("""
//            --- test_generic.lua
//
//            ---@generic T
//            ---@param list T[]
//            ---@return fun():number, T
//            local function myIterator(list)
//                local idx = 0
//                return nil -- todo
//            end
//
//            ---@type Emmy[]
//            local emmyList = {}
//
//            for i, em in myIterator(emmyList) do
//                em.--[[caret]]
//            end
//        """) {
//            assertTrue(it.contains("sayHello"))
//        }
//    }
}
