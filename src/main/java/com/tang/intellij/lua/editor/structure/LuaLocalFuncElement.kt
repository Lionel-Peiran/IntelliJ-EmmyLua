

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaLocalFuncDef
import com.tang.intellij.lua.psi.LuaPsiElement

class LuaLocalFuncElement(target: LuaPsiElement, name: String, paramSignature: String)
    : LuaFuncElement(target, name, paramSignature, LuaIcons.LOCAL_FUNCTION) {
    internal constructor(target: LuaLocalFuncDef) : this(target, target.name ?: "<??>", target.paramSignature)
}
