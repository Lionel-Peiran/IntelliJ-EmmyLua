

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.comment.psi.LuaDocClassNameRef
import com.tang.intellij.lua.comment.psi.LuaDocPsiElement
import com.tang.intellij.lua.comment.psi.LuaDocVisitor
import com.tang.intellij.lua.comment.psi.api.LuaComment
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaIndexExpr
import com.tang.intellij.lua.psi.LuaNameExpr
import com.tang.intellij.lua.psi.LuaVisitor

class LuaDeprecationInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : LuaVisitor() {
        override fun visitIndexExpr(o: LuaIndexExpr) {
            super.visitIndexExpr(o)
            val id = o.id ?: return
            checkDeprecated(o) {
                holder.registerProblem(id, "'${o.name}' is deprecated", ProblemHighlightType.LIKE_DEPRECATED)
            }
        }

        override fun visitNameExpr(o: LuaNameExpr) {
            checkDeprecated(o) {
                holder.registerProblem(o, "'${o.name}' is deprecated", ProblemHighlightType.LIKE_DEPRECATED)
            }
        }

        private inline fun checkDeprecated(o: PsiElement, action: () -> Unit) {
            val resolve = o.reference?.resolve() ?: o
            val isDeprecated = when (resolve) {
                is LuaClassMember -> resolve.isDeprecated
                is LuaDocTagClass -> resolve.isDeprecated
                else -> false
            }
            if (isDeprecated) action()
        }

        override fun visitComment(o: PsiComment) {
            super.visitComment(o)
            if (o is LuaComment) {
                o.acceptChildren(object : LuaDocVisitor() {
                    override fun visitClassNameRef(o: LuaDocClassNameRef) {
                        checkDeprecated(o) {
                            holder.registerProblem(o, "'${o.text}' is deprecated", ProblemHighlightType.LIKE_DEPRECATED)
                        }
                    }

                    override fun visitPsiElement(o: LuaDocPsiElement) {
                        o.acceptChildren(this)
                    }
                })
            }
        }
    }
}