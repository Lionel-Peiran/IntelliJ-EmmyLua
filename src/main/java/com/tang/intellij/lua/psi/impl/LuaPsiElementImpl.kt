

package com.tang.intellij.lua.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceService
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.tang.intellij.lua.psi.LuaPsiElement

/**
 * LuaPsiElement 基类
 * First Created on 2016/11/24.
 */
open class LuaPsiElementImpl internal constructor(node: ASTNode) : ASTWrapperPsiElement(node), LuaPsiElement {

    override fun getReferences(): Array<PsiReference> {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this, PsiReferenceService.Hints.NO_HINTS)
    }

    override fun getReference(): PsiReference? {
        return references.firstOrNull()
    }
}
