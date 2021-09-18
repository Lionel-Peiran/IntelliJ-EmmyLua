

/*
package com.tang.intellij.test.inspections

import com.tang.intellij.lua.codeInsight.inspection.MatchFunctionSignatureInspection

class MatchFunctionSignatureTest : LuaInspectionsTestBase(MatchFunctionSignatureInspection()) {
    fun testTypeMissMatch() = checkByText("""
        ---@param p1 number
        ---@param p2 string
        local function test(p1, p2)
        end

        test(1, <warning>2</warning>)
    """)

    fun testTooManyArgs() = checkByText("""
        ---@param p1 number
        ---@param p2 string
        local function test(p1, p2)
        end

        test(1, "2", <warning>3</warning>)
    """)

    fun testMissArgs() = checkByText("""
        local function test(p1, p2)
        end
        test(1<warning>)</warning>
    """)

    fun testMultiReturn() = checkByText("""
        local function ret_nn()
            return 1, 2
        end
        local function ret_sn()
            return "1", 2
        end
        ---@param n1 number
        ---@param n2 number
        local function acp_nn(n1, n2) end

        acp_nn(ret_nn())
        acp_nn(<warning>ret_sn()</warning>)
        acp_nn(ret_nn(), 1)
        acp_nn(<warning>ret_sn()</warning>, 1)
    """)

    fun testParentIndex() = checkByText("""
        local dummy, A = 1, {}
        ---@return number, string
        function A.a()
            return 1, "1"
        end
        ---@param n number
        ---@param s string
        local function acp_ns(n, s) end

        acp_ns(A.a())
    """)
}*/
