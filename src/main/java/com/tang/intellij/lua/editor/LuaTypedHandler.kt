

package com.tang.intellij.lua.editor

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.codeInsight.template.macro.SuggestLuaParametersMacro
import com.tang.intellij.lua.lang.LuaFileType
import com.tang.intellij.lua.psi.LuaFuncBody
import com.tang.intellij.lua.psi.LuaFuncBodyOwner
import com.tang.intellij.lua.psi.LuaTypes

/**

 * First Created on 2016/11/28.
 */
class LuaTypedHandler : TypedHandlerDelegate() {

    override fun checkAutoPopup(charTyped: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType == LuaFileType.INSTANCE) {
            if (charTyped == ':' || charTyped == '@') {
                AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, null)
                return Result.STOP
            }
            if (charTyped == '.') {
                val element = file.findElementAt(editor.caretModel.offset - 1)
                when (element?.node?.elementType) {
                    LuaTypes.DOT,
                    LuaTypes.SHORT_COMMENT -> return Result.STOP
                }
            }
        }
        return super.checkAutoPopup(charTyped, project, editor, file)
    }

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (file.fileType == LuaFileType.INSTANCE) {
            // function() <caret> end 自动加上end
            if (c == '(') {
                PsiDocumentManager.getInstance(project).commitDocument(editor.document)
                val pos = editor.caretModel.offset - 1
                val element = file.findElementAt(pos)
                if (element != null && element.parent is LuaFuncBodyOwner) {
                    val templateManager = TemplateManager.getInstance(project)
                    val template = templateManager.createTemplate("", "", "(\$PARAMETERS\$) \$END\$ end")
                    template.addVariable("PARAMETERS", MacroCallNode(SuggestLuaParametersMacro(SuggestLuaParametersMacro.Position.TypedHandler)), TextExpression(""), false)
                    templateManager.startTemplate(editor, template)
                    return Result.STOP
                }
            }
        }
        return super.charTyped(c, project, editor, file)
    }
}
