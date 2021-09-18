

package com.tang.intellij.lua.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.tang.intellij.lua.editor.completion.KeywordInsertHandler
import com.tang.intellij.lua.lang.LuaFileType

/**
 * 当打出 then else elseif end 时自动缩进
 * First Created on 2016/12/20.
 */
class LuaAutoIndentHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): TypedHandlerDelegate.Result {
        if (file.fileType == LuaFileType.INSTANCE) {
            val caretModel = editor.caretModel
            val ex = editor as EditorEx
            val highlighter = ex.highlighter
            val iterator = highlighter.createIterator(caretModel.offset - 1)

            if (!iterator.atEnd()) {
                val type = iterator.tokenType
                KeywordInsertHandler.autoIndent(type, file, project, editor.document, caretModel.offset)
            }
        }
        return super.charTyped(c, project, editor, file)
    }
}
