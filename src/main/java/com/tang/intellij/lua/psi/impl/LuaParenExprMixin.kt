

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.stubs.LuaExprPlaceStub

open class LuaParenExprMixin : LuaExprStubMixin<LuaExprPlaceStub> {

    constructor(stub: LuaExprPlaceStub, nodeType: IStubElementType<*, *>)
            : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaExprPlaceStub, nodeType: IElementType, node: ASTNode)
            : super(stub, nodeType, node)
}