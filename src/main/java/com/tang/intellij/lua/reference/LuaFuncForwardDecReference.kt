

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.tang.intellij.lua.psi.LuaFuncDef

class LuaFuncForwardDecReference(def: LuaFuncDef, val resolve: PsiElement) : PsiReferenceBase<LuaFuncDef>(def){
    override fun resolve(): PsiElement? {
        return resolve
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return myElement.setName(newElementName)
    }

    override fun getRangeInElement(): TextRange {
        val id = myElement.id!!
        val start = id.node.startOffset - myElement.node.startOffset
        return TextRange(start, start + id.textLength)
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return myElement.manager.areElementsEquivalent(resolve(), element)
    }

    override fun getVariants(): Array<Any> = emptyArray()
}