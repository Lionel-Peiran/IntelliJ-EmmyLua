

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.comment.psi.LuaDocAccessModifier
import com.tang.intellij.lua.comment.psi.api.LuaComment
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.stubs.LuaIndexExprStub

/**

 * First Created on 2017/4/12.
 */
abstract class LuaIndexExprMixin : LuaExprStubMixin<LuaIndexExprStub>, LuaExpr, LuaClassField {

    internal constructor(stub: LuaIndexExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    internal constructor(node: ASTNode) : super(node)

    internal constructor(stub: LuaIndexExprStub, nodeType: IElementType, node: ASTNode) : super(stub, nodeType, node)

    override fun getReference(): PsiReference? {
        return references.firstOrNull()
    }

    /**
     * --- some comment
     * ---@type type @ annotations
     * self.field = value
     *
     * get comment for `field`
     */
    val comment: LuaComment? get() {
        val p = parent
        if (p is LuaVarList) {
            val stat = p.parent as LuaStatement
            return stat.comment
        }
        return null
    }

    override val visibility: Visibility get() {
        val stub = this.stub
        if (stub != null)
            return stub.visibility
        return comment?.findTag(LuaDocAccessModifier::class.java)?.let {
            Visibility.get(it.text)
        } ?: Visibility.PUBLIC
    }

    override val worth: Int
        get() = LuaClassMember.WORTH_ASSIGN
}
