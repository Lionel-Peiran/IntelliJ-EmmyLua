

package com.tang.intellij.lua.editor

import com.intellij.ide.actions.QualifiedNameProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.psi.LuaFileUtil
import com.tang.intellij.lua.psi.LuaPsiFile

/**
 *
 * First Created on 2016/12/30.
 */
class LuaQualifiedNameProvider : QualifiedNameProvider {
    override fun adjustElementToCopy(psiElement: PsiElement): PsiElement? {
        return null
    }

    override fun getQualifiedName(psiElement: PsiElement): String? {
        if (psiElement is LuaPsiFile) {
            val virtualFile = psiElement.virtualFile
            val project = psiElement.project
            return LuaFileUtil.asRequirePath(project, virtualFile)
        }
        return null
    }

    override fun qualifiedNameToElement(s: String, project: Project): PsiElement? {
        return null
    }

    override fun insertQualifiedName(s: String, psiElement: PsiElement?, editor: Editor, project: Project?) {

    }
}
