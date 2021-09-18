

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.project.Project
import com.intellij.util.Processor
import com.tang.intellij.lua.psi.LuaClass
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaTypeAlias
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaAliasIndex
import com.tang.intellij.lua.stubs.index.LuaClassIndex
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.tang.intellij.lua.ty.ITyClass

class LuaShortNamesManagerImpl : LuaShortNamesManager() {

    override fun findClass(name: String, context: SearchContext): LuaClass? {
        return LuaClassIndex.find(name, context)
    }

    override fun findMember(type: ITyClass, fieldName: String, context: SearchContext): LuaClassMember? {
        return LuaClassMemberIndex.find(type, fieldName, context)
    }

    override fun processAllClassNames(project: Project, processor: Processor<String>): Boolean {
        return LuaClassIndex.processKeys(project, processor)
    }

    override fun processClassesWithName(name: String, context: SearchContext, processor: Processor<LuaClass>): Boolean {
        return LuaClassIndex.process(name, context.project, context.scope, Processor { processor.process(it) })
    }

    override fun getClassMembers(clazzName: String, context: SearchContext): Collection<LuaClassMember> {
        return LuaClassMemberIndex.instance.get(clazzName.hashCode(), context.project, context.scope)
    }

    override fun processAllMembers(type: ITyClass, fieldName: String, context: SearchContext, processor: Processor<LuaClassMember>): Boolean {
        return LuaClassMemberIndex.processAll(type, fieldName, context, processor)
    }

    override fun findAlias(name: String, context: SearchContext): LuaTypeAlias? {
        return LuaAliasIndex.find(name, context)
    }

    override fun processAllAlias(project: Project, processor: Processor<String>): Boolean {
        return LuaAliasIndex.instance.processAllKeys(project, processor)
    }
}