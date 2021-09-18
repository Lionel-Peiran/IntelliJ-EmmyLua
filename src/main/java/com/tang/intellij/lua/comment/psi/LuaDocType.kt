

package com.tang.intellij.lua.comment.psi

import com.tang.intellij.lua.ty.ITy

interface LuaDocType : LuaDocPsiElement {
    fun getType(): ITy
}