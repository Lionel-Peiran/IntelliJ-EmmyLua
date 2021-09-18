

package com.tang.intellij.lua.debugger.remote.value

import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import com.tang.intellij.lua.debugger.LuaXNumberPresentation
import com.tang.intellij.lua.debugger.LuaXStringPresentation
import com.tang.intellij.lua.debugger.LuaXValuePresentation
import com.tang.intellij.lua.highlighting.LuaHighlightingData
import org.luaj.vm2.*

/**
 *
 * First Created on 2017/4/16.
 */
class LuaRPrimitive(name: String) : LuaRValue(name) {
    private var type: String? = null
    private lateinit var data: String
    private var valuePresentation: XValuePresentation? = null

    override fun parse(data: LuaValue, desc: String) {
        this.data = data.toString()
        when (data) {
            is LuaString -> {
                type = "string"
                valuePresentation = LuaXStringPresentation(this.data)
            }
            is LuaNumber -> {
                type = "number"
                valuePresentation = LuaXNumberPresentation(this.data)
            }
            is LuaBoolean -> {
                type = "boolean"
                valuePresentation = LuaXValuePresentation(type!!, this.data, LuaHighlightingData.PRIMITIVE_TYPE)
            }
            is LuaFunction -> type = "function"
        }
    }

    override fun computePresentation(xValueNode: XValueNode, xValuePlace: XValuePlace) {
        if (valuePresentation == null) {
            xValueNode.setPresentation(null, type, data, false)
        } else {
            xValueNode.setPresentation(null, valuePresentation!!, false)
        }
    }
}
