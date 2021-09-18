

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.tang.intellij.lua.psi.LuaAssignStat
import com.tang.intellij.lua.psi.LuaIndexExpr
import com.tang.intellij.lua.psi.LuaVisitor
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.Ty
import com.tang.intellij.lua.ty.TyClass

class AssignTypeInspection : StrictInspection() {
    override fun buildVisitor(myHolder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor =
            object : LuaVisitor() {
                override fun visitAssignStat(o: LuaAssignStat) {
                    super.visitAssignStat(o)

                    val assignees = o.varExprList.exprList
                    val values = o.valueExprList?.exprList ?: listOf()
                    val searchContext = SearchContext.get(o.project)

                    // Check right number of fields/assignments
                    if (assignees.size > values.size) {
                        for (i in values.size until assignees.size) {
                            myHolder.registerProblem(assignees[i], "Missing value assignment.")
                        }
                    } else if (assignees.size < values.size) {
                        for (i in assignees.size until values.size) {
                            myHolder.registerProblem(values[i], "Nothing to assign to.")
                        }
                    } else {
                        // Try to match types for each assignment
                        for (i in 0 until assignees.size) {
                            val field = assignees[i]
                            val name = field.name ?: ""
                            val value = values[i]
                            val valueType = value.guessType(searchContext)

                            // Field access
                            if (field is LuaIndexExpr) {
                                // Get owner class
                                val parent = field.guessParentType(searchContext)

                                if (parent is TyClass) {
                                    val fieldType = parent.findMemberType(name, searchContext) ?: Ty.NIL

                                    if (!valueType.subTypeOf(fieldType, searchContext, false)) {
                                        myHolder.registerProblem(value, "Type mismatch. Required: '%s' Found: '%s'".format(fieldType, valueType))
                                    }
                                }
                            } else {
                                // Local/global var assignments, only check type if there is no comment defining it
                                if (o.comment == null) {
                                    val fieldType = field.guessType(searchContext)

                                    if (!valueType.subTypeOf(fieldType, searchContext, false)) {
                                        myHolder.registerProblem(value, "Type mismatch. Required: '%s' Found: '%s'".format(fieldType, valueType))
                                    }
                                }
                            }
                        }
                    }
                }
            }
}