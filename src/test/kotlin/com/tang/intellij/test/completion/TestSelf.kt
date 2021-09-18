

package com.tang.intellij.test.completion

class TestSelf : TestCompletionBase() {
    fun `test alias 1`() {
        doTest("""
            --- test_self.lua

            ---@class A
            ---@field aa string
            local a = {}
            
            ---@return self
            function a:create()end

            ---@class B : A
            ---@field bb string
            local b = {}
            
            local foo = b:create()
            foo.--[[caret]]
        """) {
            assertTrue("bb" in it)
        }
    }
}