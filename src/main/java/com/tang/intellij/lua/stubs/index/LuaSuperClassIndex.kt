

package com.tang.intellij.lua.stubs.index

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processor
import com.intellij.util.containers.ContainerUtil
import com.tang.intellij.lua.comment.psi.LuaDocTagClass

/**
 *
 * Created by TangZX on 2017/3/29.
 */
class LuaSuperClassIndex : StringStubIndexExtension<LuaDocTagClass>() {
    override fun getKey() = StubKeys.SUPER_CLASS

    companion object {
        val instance = LuaSuperClassIndex()

        fun process(s: String, project: Project, scope: GlobalSearchScope, processor: Processor<LuaDocTagClass>): Boolean {
            val c = StubIndex.getElements(StubKeys.SUPER_CLASS, s, project, scope, LuaDocTagClass::class.java)
            return ContainerUtil.process(c, processor)
        }
    }
}
