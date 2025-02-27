

package com.tang.intellij.lua.stubs

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.tang.intellij.lua.psi.LuaBinaryExpr
import com.tang.intellij.lua.psi.LuaElementTypes
import com.tang.intellij.lua.psi.impl.LuaBinaryExprImpl

class LuaBinaryExprType : LuaStubElementType<LuaBinaryExprStub, LuaBinaryExpr>("BINARY_EXPR") {
    override fun serialize(stub: LuaBinaryExprStub, stream: StubOutputStream) {
        stream.writeByte(stub.op)
    }

    override fun createStub(expr: LuaBinaryExpr, parent: StubElement<*>?): LuaBinaryExprStub {
        val type = expr.binaryOp.firstChild.node.elementType
        val indexOf = LuaElementTypes.BINARY_OPS.indexOf(type)
        return LuaBinaryExprStub(indexOf, parent)
    }

    override fun createPsi(stub: LuaBinaryExprStub): LuaBinaryExpr {
        return LuaBinaryExprImpl(stub, this)
    }

    override fun indexStub(stub: LuaBinaryExprStub, sink: IndexSink) {

    }

    override fun deserialize(stream: StubInputStream, parent: StubElement<*>?): LuaBinaryExprStub {
        return LuaBinaryExprStub(stream.readByte().toInt(), parent)
    }

}

class LuaBinaryExprStub(
        val op: Int,
        parent: StubElement<*>?
) : LuaStubBase<LuaBinaryExpr>(parent, LuaElementTypes.BINARY_EXPR), LuaExprStub<LuaBinaryExpr> {
    val opType: IElementType? get() = LuaElementTypes.BINARY_OPS.getOrNull(op)
}