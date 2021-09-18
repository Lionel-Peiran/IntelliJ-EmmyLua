

package com.tang.intellij.lua.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.comment.LuaCommentUtil
import com.tang.intellij.lua.comment.psi.api.LuaComment
import com.tang.intellij.lua.psi.LuaStatement

abstract class LuaStatMixin<StubT: StubElement<*>> : StubBasedPsiElementBase<StubT>, LuaStatement {
    internal constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    internal constructor(node: ASTNode) : super(node)

    internal constructor(stub: StubT, nodeType: IElementType, node: ASTNode) : super(stub, nodeType, node)

    override fun getComment(): LuaComment? {
        return LuaCommentUtil.findComment(this)
    }
}