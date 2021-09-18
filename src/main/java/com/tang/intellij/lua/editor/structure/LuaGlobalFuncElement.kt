

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaFuncDef
import com.tang.intellij.lua.psi.LuaPsiElement

class LuaGlobalFuncElement(target: LuaPsiElement, name: String, paramSignature: String)
    : LuaFuncElement(target, name, paramSignature, LuaIcons.GLOBAL_FUNCTION) {
    constructor(target: LuaFuncDef) : this(target, target.name ?: "<??>", target.paramSignature)
}
