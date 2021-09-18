

package com.tang.intellij.lua.codeInsight.postfix.templates

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorAllExpressionsWithCurrentOffset

abstract class LuaCallPostfixTemplate(private val fn:String) : StringBasedPostfixTemplate(fn, "$fn(expr)", selectorAllExpressionsWithCurrentOffset()) {

    override fun getTemplateString(psiElement: PsiElement) = "$fn(\$expr\$)"

    override fun getElementToRemove(expr: PsiElement) = expr
}

class LuaToStringPostfixTemplate : LuaCallPostfixTemplate("tostring")
class LuaToNumberPostfixTemplate : LuaCallPostfixTemplate("tonumber")