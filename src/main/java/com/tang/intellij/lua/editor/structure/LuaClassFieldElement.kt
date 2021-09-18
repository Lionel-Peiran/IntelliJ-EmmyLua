

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaPsiElement

class LuaClassFieldElement(target: LuaPsiElement, name: String)
    : LuaTreeElement(target, name, LuaIcons.CLASS_FIELD)
