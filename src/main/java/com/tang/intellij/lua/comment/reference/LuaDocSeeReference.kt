

package com.tang.intellij.lua.comment.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.util.Processor
import com.tang.intellij.lua.comment.psi.LuaDocTagSee
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.tang.intellij.lua.ty.ITyClass

class LuaDocSeeReference(see: LuaDocTagSee) :
        PsiPolyVariantReferenceBase<LuaDocTagSee>(see){

    val id = see.id!!

    override fun getRangeInElement(): TextRange {
        val start = id.node.startOffset - myElement.node.startOffset
        return TextRange(start, start + id.textLength)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val id = LuaElementFactory.createDocIdentifier(myElement.project, newElementName)
        this.id.replace(id)
        return id
    }

    override fun getVariants(): Array<Any> = emptyArray()

    override fun multiResolve(incomplete: Boolean): Array<ResolveResult> {
        val list = mutableListOf<ResolveResult>()
        val type = myElement.classNameRef?.resolveType() as ITyClass
        LuaClassMemberIndex.process(type.className, id.text, SearchContext.get(myElement.project), Processor {
            list.add(PsiElementResolveResult(it))
            true
        })
        return list.toTypedArray()
    }
}