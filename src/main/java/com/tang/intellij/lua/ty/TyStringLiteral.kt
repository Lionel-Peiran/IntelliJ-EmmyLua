

package com.tang.intellij.lua.ty

import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream

class TyStringLiteral(val content: String) : Ty(TyKind.StringLiteral) {
    override fun toString() = content
}

object TyStringLiteralSerializer : TySerializer<TyStringLiteral>() {
    override fun deserializeTy(flags: Int, stream: StubInputStream): TyStringLiteral {
        return TyStringLiteral(stream.readUTF())
    }

    override fun serializeTy(ty: TyStringLiteral, stream: StubOutputStream) {
        stream.writeUTF(ty.content)
    }
}