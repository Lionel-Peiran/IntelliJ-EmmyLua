

package com.tang.intellij.lua.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.*
import com.tang.intellij.lua.psi.LuaExpr
import com.tang.intellij.lua.psi.LuaPsiElement

interface LuaExprStubElement<out T> {
    val stub: T
}

interface LuaExprStub<T : LuaExpr> : StubElement<T>

open class LuaExprStubImpl<T : LuaExpr>(
        parent: StubElement<*>?,
        elementType: LuaStubElementType<*, *>
) : LuaStubBase<T>(parent, elementType), LuaExprStub<T>

class LuaExprPlaceStub(
        parent: StubElement<*>?,
        elementType: LuaStubElementType<*, *>
) : LuaExprStubImpl<LuaExpr>(parent, elementType) {
    class Type<PsiT : LuaPsiElement>(debugName: String, private val ctor: (LuaExprPlaceStub, IStubElementType<*, *>) -> PsiT)
        : LuaStubElementType<LuaExprPlaceStub, PsiT>(debugName) {
        override fun createStub(psi: PsiT, parentStub: StubElement<*>?): LuaExprPlaceStub {
            return LuaExprPlaceStub(parentStub, this)
        }

        override fun shouldCreateStub(node: ASTNode): Boolean {
            return createStubIfParentIsStub(node)
        }

        override fun serialize(stub: LuaExprPlaceStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?)
                = LuaExprPlaceStub(parentStub, this)

        override fun createPsi(stub: LuaExprPlaceStub): PsiT {
            return ctor(stub, this)
        }

        override fun indexStub(stub: LuaExprPlaceStub, sink: IndexSink) {
        }
    }
}