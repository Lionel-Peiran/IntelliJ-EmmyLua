

package com.tang.intellij.lua.psi.search

import com.intellij.psi.search.searches.ExtensibleQueryFactory
import com.intellij.util.Query
import com.tang.intellij.lua.psi.LuaClassMethod

/**
 *
 * First Created on 2017/3/29.
 */
class LuaOverridingMethodsSearch : ExtensibleQueryFactory<LuaClassMethod, LuaOverridingMethodsSearch.SearchParameters>("com.tang.intellij.lua") {

    class SearchParameters(val method: LuaClassMethod, val isDeep: Boolean)

    companion object {
        private val INSTANCE = LuaOverridingMethodsSearch()

        @JvmOverloads
        fun search(methodDef: LuaClassMethod, deep: Boolean = true): Query<LuaClassMethod> =
                INSTANCE.createUniqueResultsQuery(SearchParameters(methodDef, deep))
    }
}
