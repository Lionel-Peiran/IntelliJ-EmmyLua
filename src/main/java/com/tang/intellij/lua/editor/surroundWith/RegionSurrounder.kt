

package com.tang.intellij.lua.editor.surroundWith

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

/**
 * region surrounder
 * Created by tangzx on 2017/2/25.
 */
open class RegionSurrounder(
        val desc: String,
        val start: String,
        val end: String) : Surrounder {

    override fun getTemplateDescription() = desc

    override fun isApplicable(psiElements: Array<PsiElement>) = true

    override fun surroundElements(project: Project, editor: Editor, psiElements: Array<PsiElement>): TextRange {

        val last = psiElements[psiElements.size - 1]
        val lastTextRange = last.textRange

        val first = psiElements[0]
        val firstTextRange = first.textRange

        val document = editor.document
        val startLineNumber = document.getLineNumber(firstTextRange.startOffset)
        val startIndent = document.getText(TextRange(document.getLineStartOffset(startLineNumber), firstTextRange.startOffset))

        val description = "description"
        val startString = "--$start $description\n$startIndent"
        val endString = "\n$startIndent--$end"
        document.insertString(lastTextRange.endOffset, endString)
        document.insertString(firstTextRange.startOffset, startString)

        val begin = firstTextRange.startOffset + (2 + start.length + 1)
        return TextRange(begin, begin + description.length)
    }
}
