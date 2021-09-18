

package com.tang.intellij.lua.editor

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.LuaClassMethodDef
import javax.swing.Icon

/**
 * icon provider
 * First Created on 2017/4/12.
 */
class LuaIconProvider : IconProvider() {
    override fun getIcon(psiElement: PsiElement, iconFlags: Int): Icon? {
        if (psiElement is LuaDocTagClass)
            return LuaIcons.CLASS
        else if (psiElement is LuaClassMethodDef)
            return LuaIcons.CLASS_METHOD
        return null
    }
}
