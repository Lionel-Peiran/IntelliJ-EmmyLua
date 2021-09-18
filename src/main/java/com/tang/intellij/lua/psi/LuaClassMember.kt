

package com.tang.intellij.lua.psi

import com.intellij.psi.PsiNamedElement
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.ITyClass
import com.tang.intellij.lua.ty.TyUnion

interface WorthElement {
    val worth: Int
}

/**
 * Class 成员
 * First Created on 2016/12/12.
 */
interface LuaClassMember :  LuaTypeGuessable, PsiNamedElement, WorthElement {
            companion object {
                const val WORTH_DOC = 1000
                const val WORTH_METHOD_DEF = WORTH_DOC - 100
                const val WORTH_TABLE_FIELD = WORTH_DOC- 200
        const val WORTH_ASSIGN = 0
    }
    fun guessParentType(context: SearchContext): ITy
    val visibility: Visibility
    val isDeprecated: Boolean
}

fun LuaClassMember.guessClassType(context: SearchContext): ITyClass? {
    val ty = guessParentType(context)
    return TyUnion.getPerfectClass(ty)
}