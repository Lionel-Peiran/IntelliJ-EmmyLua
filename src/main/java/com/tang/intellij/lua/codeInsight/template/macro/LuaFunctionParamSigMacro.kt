

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.*
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.codeInsight.template.context.LuaFunContextType
import com.tang.intellij.lua.psi.LuaFuncBodyOwner

class LuaFunctionParamSigMacro : Macro() {
    override fun getPresentableName() = "LuaFunctionParamSignature()"

    override fun getName() = "LuaFunctionParamSignature"
    
    override fun calculateResult(expressions: Array<out Expression>, context: ExpressionContext?): Result? {
        var e = context?.psiElementAtStartOffset
        while (e != null && e !is PsiFile) {
            e = e.parent
            if (e is LuaFuncBodyOwner) {
                val str = e.paramSignature
                return TextResult(str.substring(1, str.length-1))
            }
        }
        return null
    }

    override fun calculateLookupItems(params: Array<out Expression>, context: ExpressionContext?): Array<LookupElement>? {
        var e = context?.psiElementAtStartOffset
        val list = mutableListOf<LookupElement>()
        while (e != null && e !is PsiFile) {
            e = e.parent
            if (e is LuaFuncBodyOwner) {
                val str = e.paramSignature
                list.add(LookupElementBuilder.create(str.substring(1, str.length-1)))
            }
        }
        return list.toTypedArray()
    }

    override fun isAcceptableInContext(context: TemplateContextType): Boolean {
        return context is LuaFunContextType
    }
}