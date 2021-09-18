

package com.tang.intellij.lua.stubs

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.tang.intellij.lua.comment.psi.LuaDocTagType
import com.tang.intellij.lua.comment.psi.impl.LuaDocTagTypeImpl
import com.tang.intellij.lua.psi.LuaElementType

class LuaDocTagTypeType : LuaStubElementType<LuaDocTagTypeStub, LuaDocTagType>("DOC_TY"){
    override fun indexStub(stub: LuaDocTagTypeStub, sink: IndexSink) {
    }

    override fun deserialize(inputStream: StubInputStream, stubElement: StubElement<*>?): LuaDocTagTypeStub {
        return LuaDocTagTypeStubImpl(stubElement)
    }

    override fun createPsi(stub: LuaDocTagTypeStub) = LuaDocTagTypeImpl(stub, this)

    override fun serialize(stub: LuaDocTagTypeStub, stubElement: StubOutputStream) {
    }

    override fun createStub(tagType: LuaDocTagType, stubElement: StubElement<*>?): LuaDocTagTypeStub {
        return LuaDocTagTypeStubImpl(stubElement)
    }
}

interface LuaDocTagTypeStub : StubElement<LuaDocTagType>

class LuaDocTagTypeStubImpl(parent: StubElement<*>?)
    : LuaDocStubBase<LuaDocTagType>(parent, LuaElementType.TYPE_DEF), LuaDocTagTypeStub