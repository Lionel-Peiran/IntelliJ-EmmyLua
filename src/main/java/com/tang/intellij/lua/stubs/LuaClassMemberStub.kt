

package com.tang.intellij.lua.stubs

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.StubElement
import com.tang.intellij.lua.psi.Visibility

interface LuaClassMemberStub<T : PsiElement> : StubElement<T>, LuaDocTyStub {
    val visibility: Visibility
    val isDeprecated: Boolean
}