

package com.tang.intellij.lua.editor.completion

import com.tang.intellij.lua.psi.LuaFuncBodyOwner
import com.tang.intellij.lua.psi.LuaParamInfo

/**
 *
 * First Created on 2016/12/20.
 */
open class FuncInsertHandler(private val funcBodyOwner: LuaFuncBodyOwner) : ArgsInsertHandler() {

    override fun getParams(): Array<LuaParamInfo> {
        return funcBodyOwner.params
    }

    override val isVarargs: Boolean
        get() = funcBodyOwner.funcBody?.ellipsis != null
}
