

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.Constants
import com.tang.intellij.lua.psi.LuaLiteralExpr
import com.tang.intellij.lua.psi.LuaLocalDef
import com.tang.intellij.lua.psi.LuaVisitor
import org.jetbrains.annotations.Nls

/**
 *
 * Created by tangzx on 2016/12/16.
 */
class SimplifyLocalAssignment : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : LuaVisitor() {
            override fun visitLocalDef(o: LuaLocalDef) {
                val exprList = o.exprList
                if (exprList != null) {
                    val list = exprList.exprList
                    if (list.size == 1) {
                        val expr = list[0]
                        if (expr is LuaLiteralExpr && Constants.WORD_NIL == expr.getText()) {
                            holder.registerProblem(expr, "Local assignment can be simplified", ProblemHighlightType.LIKE_UNUSED_SYMBOL, Fix())
                        }
                    }
                }
            }
        }
    }

    inner class Fix : LocalQuickFix {

        @Nls
        override fun getFamilyName(): String {
            return "Simplify local assignment"
        }

        override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
            val localDef = PsiTreeUtil.getParentOfType(problemDescriptor.endElement, LuaLocalDef::class.java)
            val assign = localDef?.assign
            val exprList = localDef?.exprList
            if (localDef != null && assign != null && exprList != null)
                localDef.deleteChildRange(assign, exprList)
        }
    }
}
