

package com.tang.intellij.lua.codeInsight.template.context

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.psi.LuaFuncBody

class LuaFunContextType : TemplateContextType("LUA_FUNCTION", "function", LuaCodeContextType::class.java) {

    override fun isInContext(psiFile: PsiFile, i: Int): Boolean {
        return PsiTreeUtil.findElementOfClassAtOffset(psiFile, i, LuaFuncBody::class.java, false) != null
    }
}