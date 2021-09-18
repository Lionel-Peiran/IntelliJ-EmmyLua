

package com.tang.intellij.lua.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import com.tang.intellij.lua.psi.LuaTableExpr
import com.tang.intellij.lua.psi.impl.LuaTableExprImpl
import com.tang.intellij.lua.psi.shouldCreateStub
import com.tang.intellij.lua.ty.getTableTypeName

/**

 * Created by tangzx on 2017/1/12.
 */
class LuaTableExprType : LuaStubElementType<LuaTableExprStub, LuaTableExpr>("TABLE_EXPR") {

    override fun createPsi(luaTableStub: LuaTableExprStub) = LuaTableExprImpl(luaTableStub, this)

    override fun createStub(tableExpr: LuaTableExpr, stubElement: StubElement<*>): LuaTableExprStub {
        val tableTypeName = getTableTypeName(tableExpr)
        return LuaTableExprStubImpl(tableTypeName, stubElement, this)
    }

    override fun shouldCreateStub(node: ASTNode): Boolean {
        val tab = node.psi as LuaTableExpr
        return tab.shouldCreateStub
    }

    override fun serialize(stub: LuaTableExprStub, stubOutputStream: StubOutputStream) {
        stubOutputStream.writeName(stub.tableTypeName)
    }

    override fun deserialize(stubInputStream: StubInputStream, stubElement: StubElement<*>): LuaTableExprStub {
        val tableTypeName = stubInputStream.readName()
        return LuaTableExprStubImpl(StringRef.toString(tableTypeName), stubElement, this)
    }

    override fun indexStub(luaTableStub: LuaTableExprStub, indexSink: IndexSink) {}
}

interface LuaTableExprStub : LuaExprStub<LuaTableExpr> {
    val tableTypeName: String
}

class LuaTableExprStubImpl(
        override val tableTypeName: String,
        parent: StubElement<*>,
        elementType: LuaStubElementType<*, *>
) : LuaExprStubImpl<LuaTableExpr>(parent, elementType), LuaTableExprStub