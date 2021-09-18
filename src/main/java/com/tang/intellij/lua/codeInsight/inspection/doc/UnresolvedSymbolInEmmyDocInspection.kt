

package com.tang.intellij.lua.codeInsight.inspection.doc

import com.intellij.codeInspection.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.tang.intellij.lua.comment.psi.LuaDocTagParam
import com.tang.intellij.lua.comment.psi.LuaDocVisitor

class UnresolvedSymbolInEmmyDocInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : LuaDocVisitor() {
            override fun visitTagParam(o: LuaDocTagParam) {
                o.paramNameRef?.let { paramNameRef ->
                    if (paramNameRef.reference.resolve() == null) {
                        holder.registerProblem(paramNameRef,
                                "Cant resolve symbol '${paramNameRef.text}'",
                                ProblemHighlightType.WEAK_WARNING,
                                object : LocalQuickFix {
                                    override fun getFamilyName() = "Remove '${paramNameRef.text}'"

                                    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
                                        o.delete()
                                    }
                                })
                    }
                }
            }
        }
    }
}