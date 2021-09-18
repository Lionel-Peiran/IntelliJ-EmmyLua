

package com.tang.intellij.lua.project

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.lang.LuaIcons
import javax.swing.Icon

class LuaSourceRootIconProvider : IconProvider() {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if (element is PsiDirectory) {
            if (LuaSourceRootManager.getInstance(element.project).isSourceRoot(element.virtualFile)) {
                return LuaIcons.ROOT
            }
        }
        return null
    }
}