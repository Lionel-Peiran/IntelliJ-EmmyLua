

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.RefactoringQuickFix
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiNamedElement
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.RefactoringActionHandlerFactory
import com.tang.intellij.lua.Constants
import com.tang.intellij.lua.psi.*

class LocalNameShadowed : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor {
        return object : LuaVisitor() {

            private fun check(namedElement: PsiNamedElement) {
                val name = namedElement.name
                if (name == Constants.WORD_UNDERLINE)
                    return

                val psi = (if (namedElement is PsiNameIdentifierOwner) namedElement.nameIdentifier else namedElement) ?: return
                val document = FileDocumentManager.getInstance().getDocument(namedElement.containingFile.virtualFile)

                LuaDeclarationTree.get(namedElement.containingFile).walkUpLocal(namedElement) {
                    if (name == it.name) {
                        val nameDef = it.psi
                        val desc = if (document != null)
                            "Local name shadowed, '$name' was previously defined on line ${document.getLineNumber(nameDef.node.startOffset) + 1}"
                        else
                            "Local name shadowed"
                        holder.registerProblem(psi, desc, object : RefactoringQuickFix {
                            override fun getHandler(): RefactoringActionHandler {
                                return RefactoringActionHandlerFactory.getInstance().createRenameHandler()
                            }

                            override fun getFamilyName(): String {
                                return "Rename"
                            }
                        })
                        return@walkUpLocal false
                    }
                    true
                }
            }

            override fun visitLocalDef(o: LuaLocalDef) {
                o.nameList?.nameDefList?.forEach {
                    val name = it.name
                    if (name != Constants.WORD_UNDERLINE) {
                        check(it)
                    }
                }
                super.visitLocalDef(o)
            }

            override fun visitLocalFuncDef(o: LuaLocalFuncDef) {
                check(o)
                super.visitLocalFuncDef(o)
            }

            override fun visitParamNameDef(o: LuaParamNameDef) {
                check(o)
            }
        }
    }
}