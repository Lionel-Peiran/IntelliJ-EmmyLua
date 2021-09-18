

package com.tang.intellij.test.completion

class ParsingTest : TestCompletionBase() {

    fun `test parse chinese non-java characters`() {
        doTest("""
            --- test_parse_chinese_characters.lua

            ---@class 类型1
            local 对象 = { 名字 = "name" }

            ---@return number, 类型1
            local function 获得一个什么东西（一）()
            end

            local 鬼知道我是什么, 天晓得我是什么 = 获得一个什么东西（一）()
            天晓得我是什么.--[[caret]]
        """) {
            assertTrue("名字" in it)
        }
    }
}