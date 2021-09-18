

package com.tang.intellij.lua.codeInsight.inspection.doc

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.tang.intellij.lua.comment.psi.LuaDocGeneralTy
import com.tang.intellij.lua.comment.psi.LuaDocType
import com.tang.intellij.lua.comment.psi.LuaDocVisitor

class UnresolvedClassInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor {
        return object : LuaDocVisitor() {
            override fun visitType(o: LuaDocType) {
                if (o is LuaDocGeneralTy) {
                    val typeRef = o.classNameRef
                    if (typeRef.reference.resolve() == null) {
                        holder.registerProblem(o, "Unresolved type \"${typeRef.text}\"", ProblemHighlightType.ERROR)
                    }
                }
            }
        }
    }
}