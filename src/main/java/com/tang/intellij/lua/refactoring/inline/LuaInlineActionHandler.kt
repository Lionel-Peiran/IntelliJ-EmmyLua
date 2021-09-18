

package com.tang.intellij.lua.refactoring.inline

import com.intellij.lang.Language
import com.intellij.lang.refactoring.InlineActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.lang.LuaLanguage

// todo: impl inline action
class LuaInlineActionHandler : InlineActionHandler() {
    override fun inlineElement(project: Project, editor: Editor, psiElement: PsiElement) {
        
    }

    override fun isEnabledForLanguage(language: Language): Boolean {
        return language == LuaLanguage.INSTANCE
    }

    override fun canInlineElement(element: PsiElement): Boolean {
        return false
    }
}