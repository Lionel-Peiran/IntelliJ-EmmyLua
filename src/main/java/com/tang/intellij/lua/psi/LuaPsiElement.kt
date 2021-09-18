

package com.tang.intellij.lua.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.lang.LuaLanguageLevel
import com.tang.intellij.lua.project.LuaSettings

/**
 * LuaPsiElement
 * First Created on 2016/11/22.
 */
interface LuaPsiElement : NavigatablePsiElement

val LuaPsiElement.moduleName: String? get() {
    val file = containingFile
    if (file is LuaPsiFile)
        return file.moduleName
    return null
}

val LuaPsiElement.languageLevel: LuaLanguageLevel get() {
    val file = containingFile
    if (file is LuaPsiFile)
        return file.languageLevel
    return LuaSettings.instance.languageLevel
}

val PsiElement.realContext: PsiElement get() {
    val file = containingFile
    if (file is LuaExprCodeFragment)
        return file.context ?: this
    return this
}