

package com.tang.intellij.lua.highlighting

import com.intellij.codeInsight.highlighting.HighlightErrorFilter
import com.intellij.psi.PsiErrorElement
import com.tang.intellij.lua.comment.psi.LuaDocPsiElement
import com.tang.intellij.lua.project.LuaSettings

/**
 * disable error highlight for doc elements
 * Created by tangzx on 2017/2/19.
 */
class LuaHighlightErrorFilter : HighlightErrorFilter() {
    override fun shouldHighlightErrorElement(psiErrorElement: PsiErrorElement): Boolean {
        val parent = psiErrorElement.parent
        if (parent is LuaDocPsiElement)
            return LuaSettings.instance.isStrictDoc
        return true
    }
}
