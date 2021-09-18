

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaClosureExpr
import com.tang.intellij.lua.stubs.LuaClosureExprStub

abstract class LuaClosureExprMixin : LuaExprStubMixin<LuaClosureExprStub>, LuaClosureExpr {

    constructor(stub: LuaClosureExprStub, nodeType: IStubElementType<*, *>)
            : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaClosureExprStub, nodeType: IElementType, node: ASTNode)
            : super(stub, nodeType, node)
}