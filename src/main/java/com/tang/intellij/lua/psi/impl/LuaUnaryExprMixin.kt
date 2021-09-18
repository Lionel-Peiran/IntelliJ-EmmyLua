

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaUnaryExpr
import com.tang.intellij.lua.stubs.LuaUnaryExprStub

abstract class LuaUnaryExprMixin: LuaExprStubMixin<LuaUnaryExprStub>, LuaUnaryExpr {
    constructor(stub: LuaUnaryExprStub, nodeType: IStubElementType<*, *>)
            : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaUnaryExprStub, nodeType: IElementType, node: ASTNode)
            : super(stub, nodeType, node)
}