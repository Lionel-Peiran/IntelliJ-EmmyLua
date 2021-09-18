

package com.tang.intellij.lua.stubs.index

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.stubs.StubIndexKey
import com.tang.intellij.lua.comment.psi.LuaDocTagAlias
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.psi.LuaClassMember

object StubKeys {
    val CLASS_MEMBER = StubIndexKey.createIndexKey<Int, LuaClassMember>("lua.index.class.member")
    val SHORT_NAME = StubIndexKey.createIndexKey<String, NavigatablePsiElement>("lua.index.short_name")
    val CLASS = StubIndexKey.createIndexKey<String, LuaDocTagClass>("lua.index.class")
    val SUPER_CLASS = StubIndexKey.createIndexKey<String, LuaDocTagClass>("lua.index.super_class")
    val ALIAS = StubIndexKey.createIndexKey<String, LuaDocTagAlias>("lua.index.alias")
}
