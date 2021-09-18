

package com.tang.intellij.lua.comment.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.comment.LuaCommentUtil
import com.tang.intellij.lua.comment.psi.LuaDocClassNameRef
import com.tang.intellij.lua.comment.psi.LuaDocFunctionTy
import com.tang.intellij.lua.comment.psi.LuaDocGenericDef
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.search.LuaShortNamesManager
import com.tang.intellij.lua.search.SearchContext

/**

 * Created by TangZX on 2016/11/29.
 */
class LuaClassNameReference(element: LuaDocClassNameRef) : PsiReferenceBase<LuaDocClassNameRef>(element) {

    override fun getRangeInElement() = TextRange(0, myElement.textLength)

    override fun isReferenceTo(element: PsiElement): Boolean {
        return myElement.manager.areElementsEquivalent(element, resolve())
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val element = LuaElementFactory.createWith(myElement.project, "---@type $newElementName")
        val classNameRef = PsiTreeUtil.findChildOfType(element, LuaDocClassNameRef::class.java)
        return myElement.replace(classNameRef!!)
    }

    override fun resolve(): PsiElement? {
        val name = myElement.text
        // generic in docFunction
        val fn = PsiTreeUtil.getParentOfType(myElement, LuaDocFunctionTy::class.java)
        var genericDefList: Collection<LuaDocGenericDef>? = fn?.genericDefList
        if (genericDefList == null || genericDefList.isEmpty()) {
            // generic in comments ?
            val comment = LuaCommentUtil.findContainer(myElement)
            genericDefList = comment.findTags(LuaDocGenericDef::class.java)
        }

        for (genericDef in genericDefList) {
            if (genericDef.name == name)
                return genericDef
        }

        return LuaShortNamesManager.getInstance(myElement.project).findTypeDef(name, SearchContext.get(myElement.project))
    }

    override fun getVariants(): Array<Any> = emptyArray()
}
