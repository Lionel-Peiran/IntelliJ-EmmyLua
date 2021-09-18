

package com.tang.intellij.lua.editor

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaShortNameIndex

/**
 * Goto Symbol
 * First Created on 2016/12/12.
 */
class LuaSymbolNavigationContributor : ChooseByNameContributor {
    override fun getNames(project: Project, b: Boolean): Array<String> {
        val nameSet = mutableSetOf<String>()
        LuaShortNameIndex.instance.processAllKeys(project) { s ->
            nameSet.add(s)
            true
        }
        return nameSet.toTypedArray()
    }

    override fun getItemsByName(s: String, s1: String, project: Project, b: Boolean): Array<NavigationItem> {
        val elements = LuaShortNameIndex.find(s, SearchContext.get(project))
        return elements.toTypedArray()
    }
}
