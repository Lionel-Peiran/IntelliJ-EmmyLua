

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.codeInsight.template.macro.SuggestTypeMacro
import com.tang.intellij.lua.comment.LuaCommentUtil
import com.tang.intellij.lua.psi.LuaLocalDef
import com.tang.intellij.lua.psi.LuaPsiTreeUtil

/**
 *
 * First Created on 2016/12/16.
 */
class CreateTypeAnnotationIntention : BaseIntentionAction() {

    override fun getFamilyName(): String {
        return text
    }

    override fun getText(): String {
        return "Create type annotation"
    }

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        val localDef = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaLocalDef::class.java, false)
        if (localDef != null) {
            val comment = localDef.comment
            return comment?.tagType == null
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val localDef = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaLocalDef::class.java, false)
        if (localDef != null) {
            LuaCommentUtil.insertTemplate(localDef, editor) { _, template ->
                template.addTextSegment("---@type ")
                val name = MacroCallNode(SuggestTypeMacro())
                template.addVariable("type", name, TextExpression("table"), true)
                template.addEndVariable()
            }
        }
    }
}