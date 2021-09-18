

package com.tang.intellij.lua.psi.search

import com.intellij.psi.search.searches.ExtensibleQueryFactory
import com.intellij.util.Query
import com.tang.intellij.lua.psi.LuaClassMethod

class LuaOverridenMethodsSearch : ExtensibleQueryFactory<LuaClassMethod, LuaOverridenMethodsSearch.SearchParameters>("com.tang.intellij.lua") {
    class SearchParameters(val method: LuaClassMethod, val deep: Boolean)

    companion object {
        private val INSTANCE = LuaOverridenMethodsSearch()

        fun search(methodDef: LuaClassMethod, deep: Boolean = true): Query<LuaClassMethod> =
                INSTANCE.createUniqueResultsQuery(SearchParameters(methodDef, deep))
    }
}