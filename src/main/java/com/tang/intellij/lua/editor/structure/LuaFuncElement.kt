

package com.tang.intellij.lua.editor.structure

import com.tang.intellij.lua.psi.LuaPsiElement
import javax.swing.Icon

/**
 * First Created on 2016/12/13.
 */
abstract class LuaFuncElement(target: LuaPsiElement, name: String, paramSignature: String, icon: Icon)
    : LuaTreeElement(target, name, icon) {
    private var text: String = name + paramSignature

    override fun getPresentableText(): String {
        return text
    }
}
