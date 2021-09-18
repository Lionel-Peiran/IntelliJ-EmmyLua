

package com.tang.intellij.lua.ext

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.progress.ProgressManager
import com.tang.intellij.lua.psi.LuaTypeGuessable
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.Ty

interface ILuaTypeInfer {
    companion object {
        private val EP_NAME = ExtensionPointName.create<ILuaTypeInfer>("com.tang.intellij.lua.luaTypeInfer")

        fun infer(target: LuaTypeGuessable, context: SearchContext): ITy {
            for (typeInfer in EP_NAME.extensions) {
                ProgressManager.checkCanceled()
                val iTy = typeInfer.inferType(target, context)
                if (!Ty.isInvalid(iTy))
                    return iTy
            }
            return Ty.UNKNOWN
        }
    }

    fun inferType(target: LuaTypeGuessable, context: SearchContext): ITy
}