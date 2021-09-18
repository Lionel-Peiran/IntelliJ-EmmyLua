

package com.tang.intellij.lua.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.psi.impl.LuaLiteralExprImpl

class LuaLiteralElementType
    : LuaStubElementType<LuaLiteralExprStub, LuaLiteralExpr>("LITERAL_EXPR") {

    override fun shouldCreateStub(node: ASTNode): Boolean {
        return createStubIfParentIsStub(node)
    }

    override fun createStub(expr: LuaLiteralExpr, parentStub: StubElement<*>?): LuaLiteralExprStub {
        val str = if (expr.kind == LuaLiteralKind.String) expr.stringValue else null
        return LuaLiteralExprStub(expr.kind, expr.tooLargerString, str, parentStub, this)
    }

    override fun serialize(stub: LuaLiteralExprStub, stream: StubOutputStream) {
        stream.writeByte(stub.kind.ordinal)
        val str = stub.string
        stream.writeBoolean(stub.tooLargerString)
        val writeStr = str != null && !stub.tooLargerString
        stream.writeBoolean(writeStr)
        if (writeStr)
            stream.writeUTF(str)
    }

    override fun deserialize(stream: StubInputStream, parentStub: StubElement<*>?): LuaLiteralExprStub {
        val kind = stream.readByte()
        val tooLargerString = stream.readBoolean()
        val hasStr = stream.readBoolean()
        val str = if (hasStr) stream.readUTF() else null
        return LuaLiteralExprStub(LuaLiteralKind.toEnum(kind), tooLargerString, str, parentStub, this)
    }

    override fun indexStub(stub: LuaLiteralExprStub, sink: IndexSink) {

    }

    override fun createPsi(stub: LuaLiteralExprStub): LuaLiteralExpr {
        return LuaLiteralExprImpl(stub, this)
    }
}

class LuaLiteralExprStub(
        val kind: LuaLiteralKind,
        val tooLargerString: Boolean,
        val string: String?,
        parent: StubElement<*>?,
        type: LuaStubElementType<*, *>
) : LuaExprStubImpl<LuaLiteralExpr>(parent, type)