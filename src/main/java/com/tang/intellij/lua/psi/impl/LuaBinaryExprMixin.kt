

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaBinaryExpr
import com.tang.intellij.lua.stubs.LuaBinaryExprStub

abstract class LuaBinaryExprMixin : LuaExprStubMixin<LuaBinaryExprStub>, LuaBinaryExpr {
    constructor(stub: LuaBinaryExprStub, nodeType: IStubElementType<*, *>)
            : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaBinaryExprStub, nodeType: IElementType, node: ASTNode)
            : super(stub, nodeType, node)
}