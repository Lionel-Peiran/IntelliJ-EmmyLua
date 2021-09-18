

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.openapi.editor.Editor
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.codeInsight.template.macro.SuggestTypeMacro
import com.tang.intellij.lua.comment.LuaCommentUtil
import com.tang.intellij.lua.comment.psi.LuaDocTagReturn
import com.tang.intellij.lua.psi.LuaCommentOwner
import com.tang.intellij.lua.psi.LuaFuncBodyOwner
import org.jetbrains.annotations.Nls


class CreateFunctionReturnAnnotationIntention : FunctionIntention() {
    override fun isAvailable(bodyOwner: LuaFuncBodyOwner, editor: Editor): Boolean {
        if (bodyOwner is LuaCommentOwner) {
            val comment = bodyOwner.comment
            return comment == null || PsiTreeUtil.getChildrenOfType(comment, LuaDocTagReturn::class.java) == null
        }
        return false
    }

    @Nls
    override fun getFamilyName() = text

    override fun getText() = "Create return annotation"

    override fun invoke(bodyOwner: LuaFuncBodyOwner, editor: Editor) {
        if (bodyOwner is LuaCommentOwner) {
            LuaCommentUtil.insertTemplate(bodyOwner, editor) { _, template ->
                template.addTextSegment("---@return ")
                val typeSuggest = MacroCallNode(SuggestTypeMacro())
                template.addVariable("returnType", typeSuggest, TextExpression("table"), false)
                template.addEndVariable()
            }
        }
    }
}