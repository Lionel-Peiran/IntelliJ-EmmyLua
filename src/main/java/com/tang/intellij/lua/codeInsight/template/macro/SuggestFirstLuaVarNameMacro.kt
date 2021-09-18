

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Macro
import com.intellij.codeInsight.template.Result
import com.tang.intellij.lua.psi.LuaDeclarationTree

/**
 *
 * Created by TangZX on 2017/4/8.
 */
class SuggestFirstLuaVarNameMacro : Macro() {
    override fun getName(): String {
        return "SuggestFirstLuaVarName"
    }

    override fun getPresentableName(): String {
        return "SuggestFirstLuaVarName()"
    }

    override fun calculateResult(expressions: Array<Expression>, expressionContext: ExpressionContext): Result? {
        return null
    }

    override fun calculateLookupItems(params: Array<out Expression>, context: ExpressionContext): Array<LookupElement>? {
        val list = mutableListOf<LookupElement>()
        val pin = context.psiElementAtStartOffset
        if (pin != null) {
            LuaDeclarationTree.get(pin.containingFile).walkUpLocal(pin) {
                list.add(LookupElementBuilder.create(it.name))
            }
        }
        return list.toTypedArray()
    }
}
