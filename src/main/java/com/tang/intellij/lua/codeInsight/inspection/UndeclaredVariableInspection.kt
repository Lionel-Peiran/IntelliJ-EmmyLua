

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.tang.intellij.lua.psi.LuaNameExpr
import com.tang.intellij.lua.psi.LuaVisitor
import com.tang.intellij.lua.psi.resolve
import com.tang.intellij.lua.search.SearchContext

class UndeclaredVariableInspection : StrictInspection() {
    override fun buildVisitor(myHolder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor =
            object : LuaVisitor() {
                override fun visitNameExpr(o: LuaNameExpr) {
                    super.visitNameExpr(o)
                    val res = resolve(o, SearchContext.get(o.project))

                    if (res == null) {
                        myHolder.registerProblem(o, "Undeclared variable '%s'.".format(o.text))
                    }
                }
            }
}