

package com.tang.intellij.lua.debugger

import com.intellij.xdebugger.frame.XSuspendContext

/**
 *
 * First Created on 2016/12/31.
 */
class LuaSuspendContext(private val active: LuaExecutionStack) : XSuspendContext() {

    override fun getActiveExecutionStack() = active
}
