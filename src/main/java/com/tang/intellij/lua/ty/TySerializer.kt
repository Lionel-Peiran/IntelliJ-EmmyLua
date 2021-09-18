

package com.tang.intellij.lua.ty

import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream

interface ITySerializer {
    fun serialize(ty: ITy, stream: StubOutputStream)
    fun deserialize(flags: Int, stream: StubInputStream): ITy
}

abstract class TySerializer<T : ITy> : ITySerializer {
    override fun serialize(ty: ITy, stream: StubOutputStream) {
        @Suppress("UNCHECKED_CAST")
        val t = ty as T
        serializeTy(t, stream)
    }

    override fun deserialize(flags: Int, stream: StubInputStream): ITy {
        return deserializeTy(flags, stream)
    }

    abstract fun deserializeTy(flags: Int, stream: StubInputStream): T

    protected abstract fun serializeTy(ty: T, stream: StubOutputStream)
}