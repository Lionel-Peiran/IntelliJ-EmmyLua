

package com.tang.intellij.lua.comment

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.comment.psi.LuaDocPsiElement
import com.tang.intellij.lua.comment.psi.api.LuaComment
import com.tang.intellij.lua.psi.LuaCommentOwner

/**
 *
 * Created by TangZX on 2016/11/24.
 */
object LuaCommentUtil {

    fun findOwner(element: LuaDocPsiElement): LuaCommentOwner? {
        val comment = findContainer(element)
        return if (comment.parent is LuaCommentOwner) comment.parent as LuaCommentOwner else null
    }

    fun findContainer(psi: LuaDocPsiElement): LuaComment {
        var element = psi
        while (true) {
            if (element is LuaComment) {
                return element
            }
            element = element.parent as LuaDocPsiElement
        }
    }

    fun findComment(element: LuaCommentOwner): LuaComment? {
        return PsiTreeUtil.getChildOfType(element, LuaComment::class.java)
    }

    fun insertTemplate(commentOwner: LuaCommentOwner, editor: Editor, action:(TemplateManager, Template) -> Unit) {
        val comment = commentOwner.comment
        val project = commentOwner.project

        val templateManager = TemplateManager.getInstance(project)
        val template = templateManager.createTemplate("", "")
        if (comment != null)
            template.addTextSegment("\n")

        action(templateManager, template)
        //template.addTextSegment(String.format("---@param %s ", parDef.name))
        //val name = MacroCallNode(SuggestTypeMacro())
        //template.addVariable("type", name, TextExpression("table"), true)
        //template.addEndVariable()

        if (comment != null) {
            editor.caretModel.moveToOffset(comment.textOffset + comment.textLength)
        } else {
            editor.caretModel.moveToOffset(commentOwner.node.startOffset)
            template.addTextSegment("\n")
        }

        templateManager.startTemplate(editor, template)
    }

    fun findComment(psi: PsiElement): LuaComment? {
        return PsiTreeUtil.getParentOfType(psi, LuaComment::class.java)
    }

    fun isComment(psi: PsiElement): Boolean {
        return findComment(psi) != null
    }
}
