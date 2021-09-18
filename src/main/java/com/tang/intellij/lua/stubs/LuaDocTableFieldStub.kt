

package com.tang.intellij.lua.stubs

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import com.tang.intellij.lua.comment.psi.LuaDocTableDef
import com.tang.intellij.lua.comment.psi.LuaDocTableField
import com.tang.intellij.lua.comment.psi.impl.LuaDocTableFieldImpl
import com.tang.intellij.lua.psi.LuaElementType
import com.tang.intellij.lua.psi.Visibility
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.getDocTableTypeName

class LuaDocTableFieldType : LuaStubElementType<LuaDocTableFieldStub, LuaDocTableField>("DOC_TABLE_FIELD_DEF") {
    override fun createPsi(stub: LuaDocTableFieldStub): LuaDocTableField {
        return LuaDocTableFieldImpl(stub, this)
    }

    override fun serialize(stub: LuaDocTableFieldStub, stream: StubOutputStream) {
        stream.writeName(stub.name)
        stream.writeTyNullable(stub.docTy)
        stream.writeName(stub.parentTypeName)
    }

    override fun deserialize(stream: StubInputStream, parent: StubElement<*>): LuaDocTableFieldStub {
        val name = stream.readName()
        val docTy = stream.readTyNullable()
        val parentTypeName = stream.readName()
        return LuaDocTableFieldStubImpl(StringRef.toString(name),
                docTy,
                StringRef.toString(parentTypeName),
                parent)
    }

    override fun createStub(tableDef: LuaDocTableField, parentStub: StubElement<*>): LuaDocTableFieldStub {
        val name = tableDef.name
        val type = tableDef.ty?.getType()
        val p = tableDef.parent as LuaDocTableDef
        val pTypeName = getDocTableTypeName(p)
        return LuaDocTableFieldStubImpl(name, type, pTypeName, parentStub)
    }

    override fun indexStub(stub: LuaDocTableFieldStub, sink: IndexSink) {
        LuaClassMemberIndex.indexStub(sink, stub.parentTypeName, stub.name)
    }
}

interface LuaDocTableFieldStub : LuaClassMemberStub<LuaDocTableField> {
    val name: String
    val parentTypeName: String
}

class LuaDocTableFieldStubImpl(
        override val name: String,
        override val docTy: ITy?,
        override val parentTypeName: String,
        parent: StubElement<*>
) : LuaDocStubBase<LuaDocTableField>(parent, LuaElementType.DOC_TABLE_FIELD_DEF), LuaDocTableFieldStub {
    override val visibility: Visibility
        get() = Visibility.PUBLIC
    override val isDeprecated: Boolean
        get() = false
}