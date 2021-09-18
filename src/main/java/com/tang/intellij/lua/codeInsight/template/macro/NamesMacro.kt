

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Macro
import com.intellij.codeInsight.template.Result

class NamesMacro(vararg val args: String) : Macro() {
    override fun getPresentableName() = "LuaNamesMacro"

    override fun getName() = "LuaNamesMacro"

    override fun calculateResult(p0: Array<out Expression>, p1: ExpressionContext?): Result? {
        return null
    }

    override fun calculateLookupItems(params: Array<out Expression>, context: ExpressionContext?): Array<LookupElement>? {
        val list = mutableListOf<LookupElement>()
        for (name in args) {
            list.add(LookupElementBuilder.create(name))
        }
        return list.toTypedArray()
    }
}