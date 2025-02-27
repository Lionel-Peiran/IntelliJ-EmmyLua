

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.util.IncorrectOperationException
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.LuaIndexExpr
import com.tang.intellij.lua.psi.resolve
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.reference.LuaResolveResult
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.intellij.util.Processor

/**
 *
 * Created by TangZX on 2016/12/4.
 */
class LuaIndexReference internal constructor(element: LuaIndexExpr, private val id: PsiElement)
    : PsiReferenceBase<LuaIndexExpr>(element), LuaClassMemberReference {

    override fun getRangeInElement(): TextRange {
        val start = id.node.startOffset - myElement.node.startOffset
        return TextRange(start, start + id.textLength)
    }

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement {
        val newId = LuaElementFactory.createIdentifier(myElement.project, newElementName)
        id.replace(newId)
        return newId
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return myElement.manager.areElementsEquivalent(resolve(), element)
    }

    override fun resolve(): PsiElement? {
        return resolve(SearchContext.get(myElement.project))
    }

    override fun resolve(context: SearchContext): PsiElement? {
        val ref = resolve(myElement, context)
        if (ref != null) {
            if (ref.containingFile == myElement.containingFile) { //优化，不要去解析 Node Tree
                if (ref.node.textRange == myElement.node.textRange) {
                    return null//自己引用自己
                }
            }
        }
        return ref
    }

    override fun multiResolve(p0: Boolean): Array<PsiElementResolveResult>? {
        val list = mutableListOf<PsiElementResolveResult>();
        val name = myElement.name?:return null;
        LuaClassMemberIndex.process(LuaClassMemberIndex.__ALL_CLASS__,name,SearchContext.get(myElement.project),Processor {
            list.add(PsiElementResolveResult(it))
            true
        },false)
        if(list.isEmpty())
            return null;
        return list.toTypedArray();
    }


    override fun getVariants(): Array<Any> = emptyArray()
}
