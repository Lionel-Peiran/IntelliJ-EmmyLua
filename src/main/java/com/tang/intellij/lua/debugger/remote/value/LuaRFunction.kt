

package com.tang.intellij.lua.debugger.remote.value

import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.tang.intellij.lua.lang.LuaIcons
import org.luaj.vm2.LuaValue

/**
 *
 * First Created on 2017/4/16.
 */
class LuaRFunction(name: String) : LuaRValue(name) {
    private var type = "function"
    private lateinit var data: String

    override fun parse(data: LuaValue, desc: String) {
        this.data = desc
    }

    override fun computePresentation(xValueNode: XValueNode, xValuePlace: XValuePlace) {
        xValueNode.setPresentation(LuaIcons.LOCAL_FUNCTION, type, data, false)
    }
}
