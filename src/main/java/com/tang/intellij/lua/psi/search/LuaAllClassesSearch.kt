

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.project.Project
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ExtensibleQueryFactory
import com.intellij.util.Query
import com.tang.intellij.lua.ty.ITyClass

/**
 *
 * First Created on 2017/3/29.
 */
class LuaAllClassesSearch : ExtensibleQueryFactory<ITyClass, LuaAllClassesSearch.SearchParameters>("com.tang.intellij.lua") {

    class SearchParameters(val searchScope: SearchScope, val project: Project)

    companion object {

        private val INSTANCE = LuaAllClassesSearch()

        fun search(searchScope: SearchScope, project: Project): Query<ITyClass> {
            return INSTANCE.createUniqueResultsQuery(SearchParameters(searchScope, project))
        }
    }
}
