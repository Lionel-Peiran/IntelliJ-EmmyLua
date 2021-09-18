

package com.tang.intellij.lua.comment.psi.api

import com.intellij.psi.PsiComment
import com.tang.intellij.lua.comment.psi.*
import com.tang.intellij.lua.psi.LuaCommentOwner
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.ITySubstitutor

/**
 * Created by Tangzx on 2016/11/21.
 *
 */
interface LuaComment : PsiComment, LuaDocPsiElement {
    val owner: LuaCommentOwner?
    val moduleName: String?
    val isDeprecated: Boolean
    fun <T : LuaDocPsiElement> findTag(t:Class<T>): T?
    fun <T : LuaDocPsiElement> findTags(t:Class<T>): Collection<T>
    fun findTags(name: String): Collection<LuaDocTagDef>
    fun getParamDef(name: String): LuaDocTagParam?
    fun getFieldDef(name: String): LuaDocTagField?
    val tagClass: LuaDocTagClass?
    val tagType: LuaDocTagType?
    val tagReturn: LuaDocTagReturn?
    fun guessType(context: SearchContext): ITy
    fun isOverride(): Boolean
    fun createSubstitutor(): ITySubstitutor?
}