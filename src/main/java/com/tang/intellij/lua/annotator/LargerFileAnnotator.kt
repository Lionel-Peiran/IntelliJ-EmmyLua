

package com.tang.intellij.lua.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.project.LuaSettings
import com.tang.intellij.lua.psi.LuaPsiFile

class LargerFileAnnotator : Annotator {
    override fun annotate(psiElement: PsiElement, annotationHolder: AnnotationHolder) {
        if (psiElement is LuaPsiFile && psiElement.tooLarger) {
            val file = psiElement.virtualFile
            val fileLimit = StringUtil.formatFileSize(LuaSettings.instance.tooLargerFileThreshold * 1024L)
            val fileSize = StringUtil.formatFileSize(file.length)
            annotationHolder
                .newAnnotation(
                    HighlightSeverity.WARNING,
                    "The file size ($fileSize) exceeds configured limit ($fileLimit). Code insight features are not available."
                )
                .range(psiElement)
                .fileLevel()
                .create()
        }
    }
}