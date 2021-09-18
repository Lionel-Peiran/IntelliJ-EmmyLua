

package com.tang.intellij.lua.stubs.index

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import com.tang.intellij.lua.comment.psi.LuaDocTagAlias
import com.tang.intellij.lua.search.SearchContext

class LuaAliasIndex : StringStubIndexExtension<LuaDocTagAlias>() {
    companion object {
        val instance = LuaAliasIndex()

        fun find(name: String, context: SearchContext): LuaDocTagAlias? {
            if (context.isDumb)
                return null
            return instance.get(name, context.project, context.scope)?.firstOrNull()
        }
    }

    override fun getKey(): StubIndexKey<String, LuaDocTagAlias> {
        return StubKeys.ALIAS
    }
}