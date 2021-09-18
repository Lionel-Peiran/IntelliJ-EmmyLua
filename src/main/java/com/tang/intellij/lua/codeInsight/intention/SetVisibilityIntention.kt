

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.codeInsight.template.macro.NamesMacro
import com.tang.intellij.lua.comment.psi.LuaDocAccessModifier
import com.tang.intellij.lua.psi.LuaClassMethodDef
import com.tang.intellij.lua.psi.LuaCommentOwner
import com.tang.intellij.lua.psi.LuaFuncBodyOwner

class SetVisibilityIntention : FunctionIntention() {

    override fun isAvailable(bodyOwner: LuaFuncBodyOwner, editor: Editor): Boolean {
        if (bodyOwner is LuaClassMethodDef) {
            return bodyOwner.comment?.findTag(LuaDocAccessModifier::class.java) == null
        }
        return false
    }

    override fun invoke(bodyOwner: LuaFuncBodyOwner, editor: Editor) {
        if (bodyOwner is LuaCommentOwner) {
            val comment = bodyOwner.comment
            val funcBody = bodyOwner.funcBody
            if (funcBody != null) {
                val templateManager = TemplateManager.getInstance(editor.project)
                val template = templateManager.createTemplate("", "")
                if (comment != null) template.addTextSegment("\n")
                template.addTextSegment("---@")
                val typeSuggest = MacroCallNode(NamesMacro("public", "protected", "private"))
                template.addVariable("visibility", typeSuggest, TextExpression("private"), false)
                template.addEndVariable()
                if (comment != null) {
                    editor.caretModel.moveToOffset(comment.textOffset + comment.textLength)
                } else {
                    template.addTextSegment("\n")
                    val e: PsiElement = bodyOwner
                    editor.caretModel.moveToOffset(e.node.startOffset)
                }
                templateManager.startTemplate(editor, template)
            }
        }
    }

    override fun getFamilyName() = text

    override fun getText() = "Set Visibility (public/protected/private)"
}