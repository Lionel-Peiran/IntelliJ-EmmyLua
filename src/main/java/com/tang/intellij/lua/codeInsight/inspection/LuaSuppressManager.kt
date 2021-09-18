

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.comment.psi.LuaDocTagSuppress
import com.tang.intellij.lua.comment.psi.LuaDocTypes
import com.tang.intellij.lua.psi.LuaCommentOwner

class LuaSuppressManager : InspectionSuppressor {
    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> {
        return emptyArray()
    }

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        val commentOwner = if (element is LuaCommentOwner) element else PsiTreeUtil.getParentOfType(element, LuaCommentOwner::class.java)
        val suppress = commentOwner?.comment?.findTag(LuaDocTagSuppress::class.java)
        if (suppress != null) {
            var child = suppress.firstChild
            while (child != null) {
                if (child.node.elementType == LuaDocTypes.ID) {
                    if (child.textMatches(toolId)) {
                        return true
                    }
                }
                child = child.nextSibling
            }
        }
        return false
    }
}