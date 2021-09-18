

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ExtensibleQueryFactory
import com.intellij.util.Processor
import com.intellij.util.Query
import com.tang.intellij.lua.comment.psi.LuaDocTagClass

/**
 *
 * First Created on 2017/3/28.
 */
class LuaClassInheritorsSearch : ExtensibleQueryFactory<LuaDocTagClass, LuaClassInheritorsSearch.SearchParameters>("com.tang.intellij.lua") {

    class SearchParameters(val searchScope: GlobalSearchScope, val project: Project, val typeName: String, val isDeep: Boolean)

    companion object {

        private val INSTANCE = LuaClassInheritorsSearch()

        @JvmOverloads
        fun search(searchScope: GlobalSearchScope, project: Project, typeName: String, deep: Boolean = true): Query<LuaDocTagClass> {
            val parameters = SearchParameters(searchScope, project, typeName, deep)
            return INSTANCE.createUniqueResultsQuery(parameters)
        }

        fun isClassInheritFrom(searchScope: GlobalSearchScope, project: Project, thiz: String, sup: String): Boolean {
            val query = search(searchScope, project, thiz)
            return !query.forEach(Processor {
                it.name != sup
            })
        }
    }
}
