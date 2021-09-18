

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.psi.LuaCallExpr
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.LuaPsiTreeUtil
import com.tang.intellij.lua.psi.LuaTypes

class AppendCallParenIntention : BaseIntentionAction() {
    override fun getFamilyName() = "Append call paren"

    override fun getText() = familyName

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false) ?: return false
        return callExpr.args.node.findChildByType(LuaTypes.LPAREN) == null
    }

    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false) ?: return
        val code = "${callExpr.expr.text}(${callExpr.args.text})"
        val file = LuaElementFactory.createFile(project, code)
        val newCall = PsiTreeUtil.findChildOfType(file, LuaCallExpr::class.java) ?: return
        callExpr.replace(newCall)
    }
}