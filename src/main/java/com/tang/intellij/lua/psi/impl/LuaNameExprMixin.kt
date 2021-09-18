

package com.tang.intellij.lua.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaClassField
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaExpr
import com.tang.intellij.lua.psi.Visibility
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.LuaNameExprStub
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.Ty

/**

 * First Created on 2017/4/12.
 */
abstract class LuaNameExprMixin : StubBasedPsiElementBase<LuaNameExprStub>, LuaExpr, LuaClassField {
    internal constructor(stub: LuaNameExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    internal constructor(node: ASTNode) : super(node)

    internal constructor(stub: LuaNameExprStub, nodeType: IElementType, node: ASTNode) : super(stub, nodeType, node)

    override fun getReference(): PsiReference? {
        return references.firstOrNull()
    }

    override fun guessParentType(context: SearchContext): ITy {
        //todo: model type
        return Ty.UNKNOWN
    }

    override val visibility: Visibility
        get() = Visibility.PUBLIC

    override val worth: Int
        get() = LuaClassMember.WORTH_ASSIGN
}
