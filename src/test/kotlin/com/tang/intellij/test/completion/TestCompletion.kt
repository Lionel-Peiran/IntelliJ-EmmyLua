

package com.tang.intellij.test.completion

/**
 *
 * Created by tangzx on 2017/4/23.
 */
class TestCompletion : TestCompletionBase() {

    fun testLocalCompletion() {
        myFixture.configureByFiles("testCompletion.lua")
        doTestWithResult(listOf("a", "b", "func1"))
    }

    fun testGlobalCompletion() {
        //test 1
        myFixture.configureByFiles("globals.lua")
        myFixture.configureByText("test.lua", "<caret>")

        doTestWithResult(listOf("gVar1", "gVar2"))

        //test 2
        myFixture.configureByFiles("globals.lua")
        myFixture.configureByText("test.lua", "gVar2.<caret>")

        doTestWithResult(listOf("aaa", "bbb", "ccc"))
    }

    fun testSelfCompletion() {
        myFixture.configureByFiles("testSelf.lua")

        doTestWithResult(listOf("self:aaa", "self:abb"))
    }

    fun testParamCompletion() {
        myFixture.configureByFiles("testParam.lua")

        doTestWithResult(listOf("param1", "param2"))
    }

    fun testAnnotation() {
        val code = "---@class MyClass\n" +
                "---@field public name string\n" +
                "local s = {}\n" +
                "function s:method()end\n" +
                "function s.staticMethod()end\n" +
                "---@type MyClass\n" +
                "local instance\n"

        // fields and methods
        myFixture.configureByText("test.lua", code + "instance.<caret>")
        doTestWithResult(listOf("name", "method", "staticMethod"))


        // methods
        myFixture.configureByText("test.lua", code + "instance:<caret>")
        doTestWithResult("method")
    }

    fun testAnnotationArray() {
        myFixture.configureByFiles("testAnnotationArray.lua", "class.lua")

        doTestWithResult(listOf("name", "age", "sayHello"))
    }

    fun testAnnotationFun() {
        myFixture.configureByFiles("testAnnotationFun.lua", "class.lua")

        doTestWithResult(listOf("name", "age", "sayHello"))
    }

    fun testAnnotationDict() {
        myFixture.configureByFiles("testAnnotationDict.lua", "class.lua")

        doTestWithResult(listOf("name", "age", "sayHello"))
    }

    fun testAnonymous() {
        doTest("""
            --- testAnonymous.lua

            local function test()
                local v = xx()
                v.pp = 123
                return v
            end
            local v = test()
            v.--[[caret]]
        """) {
            assertTrue("pp" in it)
        }
    }

    fun `test doc table 1`() {
        doTest("""
             --- doc_table_test_A.lua

             ---@return { name:string, value:number }
             function getData() end

             --- doc_table_test_B.lua
             local a = getData()
             a.--[[caret]]
        """) {
            assertTrue("name" in it)
        }
    }
}
