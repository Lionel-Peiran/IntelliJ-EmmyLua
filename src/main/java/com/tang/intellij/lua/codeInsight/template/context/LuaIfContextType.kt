

package com.tang.intellij.lua.codeInsight.template.context

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.psi.LuaIfStat

/**
 * in if statement
 * Created by TangZX on 2017/4/14.
 */
class LuaIfContextType : TemplateContextType("LUA_IF", "If statement", LuaCodeContextType::class.java) {

    override fun isInContext(psiFile: PsiFile, i: Int): Boolean {
        return PsiTreeUtil.findElementOfClassAtOffset(psiFile, i, LuaIfStat::class.java, false) != null
    }
}
