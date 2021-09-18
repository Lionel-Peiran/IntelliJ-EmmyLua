

package com.tang.intellij.test.completion

class TestShouldBe : TestCompletionBase() {

    fun `test issue #298`() {
        doTest("""
            --- test_issue_298.lua
            
            ---@overload fun(t:{aaa: string, bbb: string, ccc: string})
            ---@param aaa string
            ---@param bbb string
            ---@param ccc string
            ---@return number
            function test(aaa, bbb, ccc)
            end

            test {
                a--[[caret]]
            }
        """.trimIndent()) {
            assertTrue("aaa = " in it)
        }
    }

}