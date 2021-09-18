

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaPsiElement

class LuaLocalVarElement(target: LuaPsiElement, name: String? = null)
    : LuaVarElement(target, name ?: target.name!!, LuaIcons.LOCAL_VAR)
