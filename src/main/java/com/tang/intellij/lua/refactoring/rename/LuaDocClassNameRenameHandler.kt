

package com.tang.intellij.lua.refactoring.rename

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenameInputValidator
import com.intellij.util.ProcessingContext
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.refactoring.LuaRefactoringUtil

class LuaDocClassRenameInputValidator : RenameInputValidator {
    override fun isInputValid(newName: String, element: PsiElement, context: ProcessingContext): Boolean {
        return LuaRefactoringUtil.isLuaTypeIdentifier(newName)
    }

    override fun getPattern(): ElementPattern<out PsiElement> {
        return PlatformPatterns.psiElement(LuaDocTagClass::class.java)
    }
}