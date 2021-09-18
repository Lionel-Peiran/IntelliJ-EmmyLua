

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import com.tang.intellij.lua.psi.LuaClosureExpr
import com.tang.intellij.lua.psi.LuaFuncBodyOwner
import com.tang.intellij.lua.psi.LuaPsiTreeUtil

abstract class FunctionIntention : BaseIntentionAction() {
    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        val bodyOwner = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaFuncBodyOwner::class.java, false)
        //不对Closure生效
        if (bodyOwner == null || bodyOwner is LuaClosureExpr)
            return false

        //不在body内
        val contains = bodyOwner.funcBody?.textRange?.contains(editor.caretModel.offset)
        if (contains != null && contains)
            return false

        return isAvailable(bodyOwner, editor)
    }

    abstract fun isAvailable(bodyOwner: LuaFuncBodyOwner, editor: Editor): Boolean

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val bodyOwner = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaFuncBodyOwner::class.java, false)
        if (bodyOwner != null) invoke(bodyOwner, editor)
    }

    abstract fun invoke(bodyOwner: LuaFuncBodyOwner, editor: Editor)
}