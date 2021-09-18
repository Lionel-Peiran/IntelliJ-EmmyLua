

package com.tang.intellij.lua.codeInsight.template.macro

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.*
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.codeInsight.template.context.LuaFunContextType
import com.tang.intellij.lua.psi.*

class LuaCurrentFunctionNameMacro : Macro() {
    override fun getPresentableName() = "LuaCurrentFunctionName()"

    override fun getName() = "LuaCurrentFunctionName"

    override fun calculateResult(expressions: Array<out Expression>, context: ExpressionContext?): Result? {
        var e = context?.psiElementAtStartOffset
        while (e != null && e !is PsiFile) {
            e = e.parent
            when (e) {
                is LuaClassMethodDef -> return TextResult(e.classMethodName.text)
                is LuaFuncDef -> return TextResult(e.name ?: "")
                is LuaLocalFuncDef -> return TextResult(e.name ?: "")
            }
        }
        return null
    }

    override fun calculateLookupItems(params: Array<out Expression>, context: ExpressionContext?): Array<LookupElement>? {
        var e = context?.psiElementAtStartOffset
        val list = mutableListOf<LookupElement>()
        while (e != null && e !is PsiFile) {
            e = e.parent
            when (e) {
                is LuaClassMethodDef -> list.add(LookupElementBuilder.create(e.classMethodName.text))
                is LuaFuncDef -> e.name?.let { list.add(LookupElementBuilder.create(it)) }
                is LuaLocalFuncDef -> e.name?.let { list.add(LookupElementBuilder.create(it)) }
            }
        }
        return list.toTypedArray()
    }

    override fun isAcceptableInContext(context: TemplateContextType): Boolean {
        return context is LuaFunContextType
    }
}