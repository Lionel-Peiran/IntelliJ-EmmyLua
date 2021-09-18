

package com.tang.intellij.lua.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.psi.LuaBlock
import com.tang.intellij.lua.psi.LuaPsiElement
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.Ty

abstract class LuaStubElementType<StubT : StubElement<*>, PsiT : LuaPsiElement>(debugName: String)
    : IStubElementType<StubT, PsiT>(debugName, LuaLanguage.INSTANCE) {

    protected fun createStubIfParentIsStub(node: ASTNode): Boolean {
        var parent = node.treeParent
        if (parent.psi is LuaBlock)
            parent = parent.treeParent

        val parentType = parent.elementType
        return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) ||
                parentType is IStubFileElementType<*>
    }

    override fun getExternalId() = "lua.${super.toString()}"
}

abstract class LuaStubBase<T : PsiElement>(parent: StubElement<*>?, type: LuaStubElementType<*, *>)
    : StubBase<T>(parent, type) {
    override fun toString(): String {
        return "${super.toString()}($stubType)"
    }
}

class LuaPlaceholderStub(parent: StubElement<*>?, elementType: LuaStubElementType<*, *>)
    : LuaStubBase<LuaPsiElement>(parent, elementType) {

    class Type<PsiT : LuaPsiElement>(debugName: String, private val ctor: (LuaPlaceholderStub, IStubElementType<*, *>) -> PsiT)
        : LuaStubElementType<LuaPlaceholderStub, PsiT>(debugName) {
        override fun createStub(psi: PsiT, parentStub: StubElement<*>?): LuaPlaceholderStub {
            return LuaPlaceholderStub(parentStub, this)
        }

        override fun shouldCreateStub(node: ASTNode): Boolean {
            return createStubIfParentIsStub(node)
        }

        override fun serialize(stub: LuaPlaceholderStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?)
                = LuaPlaceholderStub(parentStub, this)

        override fun createPsi(stub: LuaPlaceholderStub): PsiT {
            return ctor(stub, this)
        }

        override fun indexStub(stub: LuaPlaceholderStub, sink: IndexSink) {
        }
    }
}

abstract class LuaDocStubBase<T : PsiElement>(parent: StubElement<*>?, type: LuaStubElementType<*, *>)
    : LuaStubBase<T>(parent, type)