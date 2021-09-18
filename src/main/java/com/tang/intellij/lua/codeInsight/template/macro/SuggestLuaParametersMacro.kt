

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Macro
import com.intellij.codeInsight.template.Result
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITyFunction

class SuggestLuaParametersMacro(private val position: Position = Position.TemplateHandler) : Macro() {

    enum class Position {
        TemplateHandler, KeywordInsertHandler, TypedHandler
    }

    override fun getPresentableName(): String {
        return "SuggestLuaParameters()"
    }

    override fun getName(): String {
        return "SuggestLuaParameters"
    }

    override fun calculateResult(expressions: Array<Expression>, expressionContext: ExpressionContext): Result? {

        return null
    }

    override fun calculateLookupItems(expressions: Array<out Expression>, context: ExpressionContext): Array<LookupElement>? {
        val element = context.psiElementAtStartOffset
        val next = element?.nextSibling
        val func = when (position) {
            Position.TypedHandler,
            Position.KeywordInsertHandler -> next?.parent
            else -> next
        }

        if (func is LuaClosureExpr) {
            val ty = func.shouldBe(SearchContext.get(context.project)) as? ITyFunction ?: return null
            return create(ty.mainSignature.params)
        } else if (func is LuaClassMethod) {
            val method = func.findOverridingMethod(SearchContext.get(context.project))
            val params = method?.params
            if (params != null)
                return create(params)
        }

        return null
    }

    private fun create(params: Array<LuaParamInfo>): Array<LookupElement> {
        val paramNames = mutableListOf<String>()
        params.forEach {
            paramNames.add(it.name)
        }
        val builder = LookupElementBuilder.create(paramNames.joinToString(", "))
        return arrayOf(builder)
    }
}