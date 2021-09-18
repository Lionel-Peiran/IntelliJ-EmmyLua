

package com.tang.intellij.lua.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaExpr
import com.tang.intellij.lua.stubs.LuaTableExprStub

/**

 * Created by Administrator on 2017/6/21.
 */
open class LuaTableExprMixin : StubBasedPsiElementBase<LuaTableExprStub>, LuaExpr {
    constructor(stub: LuaTableExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaTableExprStub, nodeType: IElementType, node: ASTNode) : super(stub, nodeType, node)
}
