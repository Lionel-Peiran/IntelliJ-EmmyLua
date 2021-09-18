

package com.tang.intellij.test.inspections

import com.tang.intellij.lua.codeInsight.inspection.LocalNameShadowed

class LocalNameShadowedTest : LuaInspectionsTestBase(LocalNameShadowed()) {

    fun testLocalNameHidesPrevious() = checkByText("""
        local var1 = 1
        print(var1)
        local <warning>var1</warning> = "123"
    """)

}