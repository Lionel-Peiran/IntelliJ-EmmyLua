

package com.tang.intellij.lua.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.tang.intellij.lua.search.SearchContext

/**
 *
 * Created by tangzx on 2017/4/9.
 */
interface LuaReference : PsiReference {
    fun resolve(context: SearchContext): PsiElement?
}

interface LuaClassMemberReference : PsiPolyVariantReference {
    fun resolve(context: SearchContext): PsiElement?
}
