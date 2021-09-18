

package com.tang.intellij.test.inspections

import com.tang.intellij.lua.codeInsight.inspection.ReturnTypeInspection

class ReturnTypeInspectionTest : LuaInspectionsTestBase(ReturnTypeInspection()) {

    fun `test return 1`() = checkByText("""
        ---@return string
        local function test()
            <warning>return 1</warning>
        end
    """)

    fun `test return 2`() = checkByText("""
        ---@return string
        local function test()
            return "right"
        end
    """)

    fun `test non return`() = checkByText("""
        ---@return string
        local function test<warning>()
        end</warning>
    """)
}