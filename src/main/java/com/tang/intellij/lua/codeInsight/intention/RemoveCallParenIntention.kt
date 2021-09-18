

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.psi.*

class RemoveCallParenIntention : BaseIntentionAction() {
    override fun getFamilyName() = "Remove call paren"

    override fun getText() = familyName

    private fun findArgExpr(editor: Editor, psiFile: PsiFile): LuaExpr? {
        val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false)
        if (callExpr != null) {
            val args = callExpr.args as? LuaListArgs ?: return null
            val list = args.exprList
            if (list.isEmpty() || list.size > 1)
                return null
            val expr = list.first()
            when (expr) {
                is LuaTableExpr -> return expr
                is LuaLiteralExpr -> return if (expr.kind == LuaLiteralKind.String) expr else null
            }
        }
        return null
    }

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        return findArgExpr(editor, psiFile) != null
    }

    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false) ?: return
        val argExpr = findArgExpr(editor, psiFile) ?: return
        val code = "${callExpr.expr.text} ${argExpr.text}"
        val file = LuaElementFactory.createFile(project, code)
        val newCall = PsiTreeUtil.findChildOfType(file, LuaCallExpr::class.java) ?: return
        callExpr.replace(newCall)
    }
}