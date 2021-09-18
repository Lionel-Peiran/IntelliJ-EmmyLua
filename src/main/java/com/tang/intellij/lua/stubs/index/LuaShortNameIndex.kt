

package com.tang.intellij.lua.stubs.index

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.stubs.StringStubIndexExtension
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.search.SearchContext

/**
 *
 * First Created on 2017/1/19.
 */
class LuaShortNameIndex : StringStubIndexExtension<NavigatablePsiElement>() {

    override fun getVersion(): Int {
        return LuaLanguage.INDEX_VERSION
    }

    override fun getKey() = StubKeys.SHORT_NAME

    companion object {
        val instance = LuaShortNameIndex()

        fun find(key: String, searchContext: SearchContext): Collection<NavigatablePsiElement> {
            return if (searchContext.isDumb) emptyList() else instance.get(key, searchContext.project, searchContext.scope)
        }
    }
}
