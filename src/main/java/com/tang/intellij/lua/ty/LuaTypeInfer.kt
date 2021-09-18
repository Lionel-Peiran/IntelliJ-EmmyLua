

package com.tang.intellij.lua.ty

import com.tang.intellij.lua.ext.ILuaTypeInfer
import com.tang.intellij.lua.psi.LuaTypeGuessable
import com.tang.intellij.lua.search.SearchContext

class LuaTypeInfer : ILuaTypeInfer {
    override fun inferType(target: LuaTypeGuessable, context: SearchContext): ITy {
        return inferInner(target, context)
    }
}