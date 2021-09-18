

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.psi.*

/**
 * true <-> false
 */
class InvertBooleanIntention : BaseIntentionAction() {
    override fun getFamilyName() = text

    override fun getText() = "Invert boolean value"

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        val element = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaLiteralExpr::class.java, false)
        if (element is LuaLiteralExpr && element.kind == LuaLiteralKind.Bool) {
            return true
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val element = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaLiteralExpr::class.java, false)
        if (element is LuaLiteralExpr && element.kind == LuaLiteralKind.Bool) {
            val lit = LuaElementFactory.createLiteral(project, if (element.text == "true") "false" else "true")
            element.replace(lit)
        }
    }
}