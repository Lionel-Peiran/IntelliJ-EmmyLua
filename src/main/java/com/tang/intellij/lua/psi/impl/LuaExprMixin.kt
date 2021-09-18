

package com.tang.intellij.lua.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaExpr
import com.tang.intellij.lua.stubs.LuaExprStubImpl

/**
 * 表达式基类
 */
abstract class LuaExprMixin : LuaExprStubMixin<LuaExprStubImpl<*>>, LuaExpr {

    constructor(stub: LuaExprStubImpl<*>, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    constructor(node: ASTNode) : super(node)

    constructor(stub: LuaExprStubImpl<*>, nodeType: IElementType, node: ASTNode) : super(stub, nodeType, node)
}
