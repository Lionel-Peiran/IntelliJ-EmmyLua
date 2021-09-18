

package com.tang.intellij.lua.psi

import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.ITyClass

interface LuaTypeDef : PsiElement, NavigationItem {
    val type: ITy
}

interface LuaClass : LuaTypeDef {
    override val type: ITyClass
}

interface LuaTypeAlias : LuaTypeDef