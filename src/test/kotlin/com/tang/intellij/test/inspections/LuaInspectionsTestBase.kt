

package com.tang.intellij.test.inspections

import com.tang.intellij.test.LuaTestBase
import com.intellij.codeInspection.LocalInspectionTool
import org.intellij.lang.annotations.Language

abstract class LuaInspectionsTestBase(private val inspection: LocalInspectionTool) : LuaTestBase() {
    private fun enableInspection() =
            myFixture.enableInspections(inspection.javaClass)

    protected fun checkByText(
            @Language("Lua") text: String,
            checkWarn: Boolean = true, checkInfo: Boolean = false, checkWeakWarn: Boolean = false) {
        myFixture.configureByText("main.lua", text)
        enableInspection()
        myFixture.checkHighlighting(checkWarn, checkInfo, checkWeakWarn)
    }
}