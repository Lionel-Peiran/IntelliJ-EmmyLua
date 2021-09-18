

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.LuaNameExpr
import com.tang.intellij.lua.psi.resolve
import com.tang.intellij.lua.search.SearchContext

/**
 *
 * Created by tangzx on 2016/11/26.
 */
class LuaNameReference internal constructor(element: LuaNameExpr) : PsiReferenceBase<LuaNameExpr>(element), LuaReference {
    private val id: PsiElement = element.id

    override fun getRangeInElement(): TextRange {
        val start = id.textOffset - myElement.textOffset
        return TextRange(start, start + id.textLength)
    }

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement {
        val newId = LuaElementFactory.createIdentifier(myElement.project, newElementName)
        id.replace(newId)
        return newId
    }

    override fun resolve(): PsiElement? {
        return resolve(SearchContext.get(myElement.project))
    }

    override fun resolve(context: SearchContext): PsiElement? {
        val resolve = resolve(myElement, context)
        return if (resolve === myElement) null else resolve
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return myElement.manager.areElementsEquivalent(element, resolve())
    }

    override fun getVariants(): Array<Any> {
        return emptyArray()
    }
}
