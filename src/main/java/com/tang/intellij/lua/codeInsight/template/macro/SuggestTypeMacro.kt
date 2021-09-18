

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Macro
import com.intellij.codeInsight.template.Result
import com.tang.intellij.lua.editor.completion.LuaLookupElement
import java.util.*

/**
 * SuggestTypeMacro
 * Created by TangZX on 2016/12/16.
 */
class SuggestTypeMacro : Macro() {
    override fun getName(): String {
        return "SuggestTypeMacro"
    }

    override fun getPresentableName(): String {
        return "SuggestTypeMacro"
    }

    override fun calculateResult(expressions: Array<Expression>, expressionContext: ExpressionContext): Result? {
        return null
    }

    override fun calculateLookupItems(params: Array<Expression>, context: ExpressionContext): Array<LookupElement>? {
        val list = ArrayList<LookupElement>()
        LuaLookupElement.fillTypes(context.project, list)
        return list.toTypedArray()
    }
}
