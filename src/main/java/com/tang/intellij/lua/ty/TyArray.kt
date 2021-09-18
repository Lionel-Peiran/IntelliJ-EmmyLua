

package com.tang.intellij.lua.ty

import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.tang.intellij.lua.search.SearchContext

interface ITyArray : ITy {
    val base: ITy
}

class TyArray(override val base: ITy) : Ty(TyKind.Array), ITyArray {

    override fun equals(other: Any?): Boolean {
        return other is ITyArray && base == other.base
    }

    override fun hashCode(): Int {
        return displayName.hashCode()
    }

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {
        return super.subTypeOf(other, context, strict) || (other is TyArray && base.subTypeOf(other.base, context, strict)) || other == TABLE
    }

    override fun substitute(substitutor: ITySubstitutor): ITy {
        return TyArray(base.substitute(substitutor))
    }

    override fun accept(visitor: ITyVisitor) {
        visitor.visitArray(this)
    }

    override fun acceptChildren(visitor: ITyVisitor) {
        base.accept(visitor)
    }
}

object TyArraySerializer : TySerializer<ITyArray>() {
    override fun serializeTy(ty: ITyArray, stream: StubOutputStream) {
        Ty.serialize(ty.base, stream)
    }

    override fun deserializeTy(flags: Int, stream: StubInputStream): ITyArray {
        val base = Ty.deserialize(stream)
        return TyArray(base)
    }
}