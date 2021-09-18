

package com.tang.intellij.lua.codeInsight.postfix.templates

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorTopmost

class LuaIfNotPostfixTemplate : StringBasedPostfixTemplate("if_not", "if not expr then end", selectorTopmost()) {
    override fun getTemplateString(psiElement: PsiElement): String? {
        return "if not \$expr$ then\n\$END$\nend"
    }

    override fun getElementToRemove(expr: PsiElement): PsiElement {
        return expr
    }
}