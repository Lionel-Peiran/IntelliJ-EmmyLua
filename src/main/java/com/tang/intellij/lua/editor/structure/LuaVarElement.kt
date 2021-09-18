

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.psi.LuaPsiElement
import javax.swing.Icon

abstract class LuaVarElement(target: LuaPsiElement, name: String, icon: Icon)
    : LuaTreeElement(target, name, icon)
