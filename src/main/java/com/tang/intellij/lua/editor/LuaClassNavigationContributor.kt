

package com.tang.intellij.lua.editor

import com.intellij.navigation.GotoClassContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaClassIndex

/**
 * Goto Class
 * First Created on 2016/12/12.
 */
class LuaClassNavigationContributor : GotoClassContributor {
    override fun getQualifiedName(navigationItem: NavigationItem): String? = null

    override fun getQualifiedNameSeparator(): String? {
        return "."
    }

    override fun getNames(project: Project, b: Boolean): Array<String> {
        val allClasses = LuaClassIndex.instance.getAllKeys(project)
        return allClasses.toTypedArray()
    }

    override fun getItemsByName(s: String, s1: String, project: Project, b: Boolean): Array<NavigationItem> {
        val classDef = LuaClassIndex.find(s, SearchContext.get(project))
        return if (classDef == null)
            emptyArray()
        else
            arrayOf(classDef)
    }
}
