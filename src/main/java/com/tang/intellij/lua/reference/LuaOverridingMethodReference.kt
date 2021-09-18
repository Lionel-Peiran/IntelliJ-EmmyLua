

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.tang.intellij.lua.psi.LuaClassMethod

class LuaOverridingMethodReference(val methodDef: LuaClassMethod, val target: LuaClassMethod)
    : PsiReferenceBase<LuaClassMethod>(methodDef) {
    override fun getVariants(): Array<Any> {
        return arrayOf()
    }

    override fun getRangeInElement(): TextRange {
        val id = methodDef.nameIdentifier!!
        val start = id.node.startOffset - myElement.node.startOffset
        return TextRange(start, start + id.textLength)
    }

    override fun resolve(): PsiElement = target
}