

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaClassMethodDef
import com.tang.intellij.lua.psi.LuaPsiElement
import com.tang.intellij.lua.psi.Visibility

class LuaClassMethodElement(target: LuaPsiElement, name: String, paramSignature: String, visibility: Visibility = Visibility.PUBLIC)
    : LuaFuncElement(target, name, paramSignature, visibility.warpIcon(LuaIcons.CLASS_METHOD)) {
    internal constructor(target: LuaClassMethodDef, visibility: Visibility) : this(target, target.name!!, target.paramSignature, visibility)
}

