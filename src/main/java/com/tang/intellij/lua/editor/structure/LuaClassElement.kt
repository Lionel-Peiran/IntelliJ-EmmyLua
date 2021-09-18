

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.lang.LuaIcons

/**
 * First Created on 2016/12/13.
 */
class LuaClassElement(docTagClass: LuaDocTagClass, className: String? = null)
    : LuaVarElement(docTagClass, className ?: docTagClass.name, LuaIcons.CLASS)
