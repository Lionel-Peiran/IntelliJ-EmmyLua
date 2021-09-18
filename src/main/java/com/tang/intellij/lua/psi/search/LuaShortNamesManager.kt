

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.util.Processor
import com.tang.intellij.lua.psi.LuaClass
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaTypeAlias
import com.tang.intellij.lua.psi.LuaTypeDef
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITyClass

abstract class LuaShortNamesManager {
    companion object {
        val EP_NAME = ExtensionPointName.create<LuaShortNamesManager>("com.tang.intellij.lua.luaShortNamesManager")

        private val KEY = Key.create<LuaShortNamesManager>("com.tang.intellij.lua.luaShortNamesManager")

        fun getInstance(project: Project): LuaShortNamesManager {
            var instance = project.getUserData(KEY)
            if (instance == null) {
                instance = CompositeLuaShortNamesManager()
                project.putUserData(KEY, instance)
            }
            return instance
        }
    }

    abstract fun findClass(name: String, context: SearchContext): LuaClass?

    abstract fun findMember(type: ITyClass, fieldName: String, context: SearchContext): LuaClassMember?

    abstract fun processAllClassNames(project: Project, processor: Processor<String>): Boolean

    abstract fun processClassesWithName(name: String, context: SearchContext, processor: Processor<LuaClass>): Boolean

    abstract fun getClassMembers(clazzName: String, context: SearchContext): Collection<LuaClassMember>

    abstract fun processAllMembers(type: ITyClass, fieldName: String, context: SearchContext, processor: Processor<LuaClassMember>): Boolean

    open fun findAlias(name: String, context: SearchContext): LuaTypeAlias? {
        return null
    }

    open fun processAllAlias(project: Project, processor: Processor<String>): Boolean {
        return true
    }

    open fun findTypeDef(name: String, context: SearchContext): LuaTypeDef? {
        return findClass(name, context) ?: findAlias(name, context)
    }
}