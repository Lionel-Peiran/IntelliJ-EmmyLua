

package com.tang.intellij.lua.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectAndLibrariesScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.util.Processor
import com.intellij.util.containers.ContainerUtil
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.search.SearchContext

/**
 *
 * First Created on 2016/11/28.
 */
class LuaClassIndex : StringStubIndexExtension<LuaDocTagClass>() {

    override fun getVersion(): Int {
        return LuaLanguage.INDEX_VERSION
    }

    override fun getKey() = StubKeys.CLASS

    companion object {
        val instance = LuaClassIndex()

        fun find(name: String, context: SearchContext): LuaDocTagClass? {
            if (context.isDumb)
                return null
            return find(name, context.project, context.scope)
        }

        fun find(name: String, project: Project, scope: GlobalSearchScope): LuaDocTagClass? {
            var tagClass: LuaDocTagClass? = null
            process(name, project, scope, Processor {
                tagClass = it
                false
            })
            return tagClass
        }

        fun process(key: String, project: Project, scope: GlobalSearchScope, processor: Processor<LuaDocTagClass>): Boolean {
            val collection = instance.get(key, project, scope)
            return ContainerUtil.process(collection, processor)
        }

        fun processKeys(project: Project, processor: Processor<String>): Boolean {
            val scope = ProjectAndLibrariesScope(project)
            val allKeys = instance.getAllKeys(project)
            for (key in allKeys) {
                val ret = process(key, project, scope, Processor { false })
                if (!ret && !processor.process(key))
                    return false
            }
            return true
        }
    }
}
