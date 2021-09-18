

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.util.IncorrectOperationException
import com.intellij.util.Processor
import com.tang.intellij.lua.lang.type.LuaString
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.LuaIndexExpr
import com.tang.intellij.lua.psi.LuaLiteralExpr
import com.tang.intellij.lua.psi.resolve
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex

class LuaIndexBracketReference internal constructor(element: LuaIndexExpr, private val id: LuaLiteralExpr)
    : PsiReferenceBase<LuaIndexExpr>(element), LuaClassMemberReference {

    private val content:LuaString = LuaString.getContent(id.text)

    override fun getRangeInElement(): TextRange {
        var start = id.node.startOffset - myElement.node.startOffset
        start += content.start
        return TextRange(start, start + content.length)
    }

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement {
        val text = id.text
        val newText = text.substring(0, content.start) + newElementName + text.substring(content.end)
        val newId = LuaElementFactory.createLiteral(myElement.project, newText)
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
        val ref = resolve(myElement, content.value, context)
        if (ref != null) {
            if (ref.containingFile == myElement.containingFile) { //优化，不要去解析 Node Tree
                if (ref.node.textRange == myElement.node.textRange) {
                    return null//自己引用自己
                }
            }
        }
        return ref
    }

    override fun getVariants(): Array<Any> {
        return arrayOf()
    }

    override fun multiResolve(p0: Boolean): Array<ResolveResult>? {
        // TODO("Not yet implemented")
        val list = mutableListOf<PsiElementResolveResult>();
        val name = myElement.name?:return null;
        LuaClassMemberIndex.process(LuaClassMemberIndex.__ALL_CLASS__,name,SearchContext.get(myElement.project),
            Processor {
            list.add(PsiElementResolveResult(it))
            true
        },false)
        if(list.isEmpty())
            return null;
        return list.toTypedArray();
    }
}
