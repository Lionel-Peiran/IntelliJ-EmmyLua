

package com.tang.intellij.lua.stubs

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaBinaryExpr
import com.tang.intellij.lua.psi.LuaElementTypes
import com.tang.intellij.lua.psi.LuaUnaryExpr
import com.tang.intellij.lua.psi.impl.LuaUnaryExprImpl

class LuaUnaryExprType : LuaStubElementType<LuaUnaryExprStub, LuaUnaryExpr>("UNARY_EXPR") {
    override fun serialize(stub: LuaUnaryExprStub, stream: StubOutputStream) {
        stream.writeByte(stub.opIndex)
    }

    override fun createStub(expr: LuaUnaryExpr, parent: StubElement<*>?): LuaUnaryExprStub {
        val operator = expr.unaryOp.node.firstChildNode.elementType
        val index = LuaElementTypes.UNARY_OPS.indexOf(operator)
        return LuaUnaryExprStub(index, parent)
    }

    override fun createPsi(stub: LuaUnaryExprStub): LuaUnaryExpr {
        return LuaUnaryExprImpl(stub, this)
    }

    override fun indexStub(stub: LuaUnaryExprStub, sink: IndexSink) {

    }

    override fun deserialize(stream: StubInputStream, parent: StubElement<*>?): LuaUnaryExprStub {
        val index = stream.readByte().toInt()
        return LuaUnaryExprStub(index, parent)
    }
}

class LuaUnaryExprStub(
        val opIndex: Int,
        parent: StubElement<*>?
) : LuaStubBase<LuaBinaryExpr>(parent, LuaElementTypes.UNARY_EXPR), LuaExprStub<LuaBinaryExpr> {
    val opType: IElementType? get() = LuaElementTypes.UNARY_OPS.getOrNull(opIndex)
}