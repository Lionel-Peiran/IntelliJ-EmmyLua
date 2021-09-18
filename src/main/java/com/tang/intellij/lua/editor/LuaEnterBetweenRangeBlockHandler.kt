

package com.tang.intellij.lua.editor

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.util.IncorrectOperationException
import com.intellij.util.text.CharArrayUtil
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.psi.LuaBlock
import com.tang.intellij.lua.psi.LuaIndentRange

class LuaEnterBetweenRangeBlockHandler : EnterHandlerDelegateAdapter() {

    override fun preprocessEnter(psiFile: PsiFile,
                                 editor: Editor,
                                 caretOffsetRef: Ref<Int>,
                                 caretAdvance: Ref<Int>,
                                 dataContext: DataContext,
                                 originalHandler: EditorActionHandler?): EnterHandlerDelegate.Result {
        if (!psiFile.language.`is`(LuaLanguage.INSTANCE)) {
            return EnterHandlerDelegate.Result.Continue
        }

        val caretOffset = caretOffsetRef.get()
        val document = editor.document
        val text = document.charsSequence
        val prevCharOffset = CharArrayUtil.shiftBackward(text, caretOffset - 1, " \t")
        val nextCharOffset = CharArrayUtil.shiftForward(text, caretOffset, " \t")

        val prev = psiFile.findElementAt(prevCharOffset)
        val next = psiFile.findElementAt(nextCharOffset)
        if (prev != next &&
                prev != null &&
                next != null &&
                prev.parent == next.parent &&
                prev.parent is LuaIndentRange &&
                prev.nextSibling is LuaBlock) {
            originalHandler?.execute(editor, editor.caretModel.currentCaret, dataContext)
            PsiDocumentManager.getInstance(psiFile.project).commitDocument(editor.document)
            try {
                CodeStyleManager.getInstance(psiFile.project).adjustLineIndent(psiFile, editor.caretModel.offset)
            } catch (e: IncorrectOperationException) {
            }

            return EnterHandlerDelegate.Result.DefaultForceIndent
        }
        return EnterHandlerDelegate.Result.Continue
    }
}