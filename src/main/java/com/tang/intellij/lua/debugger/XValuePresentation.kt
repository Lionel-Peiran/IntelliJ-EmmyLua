

package com.tang.intellij.lua.debugger

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.xdebugger.frame.presentation.XNumericValuePresentation
import com.intellij.xdebugger.frame.presentation.XStringValuePresentation
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import com.tang.intellij.lua.highlighting.LuaHighlightingData

open class LuaXValuePresentation(val sType: String, val sValue:String, val tkey : TextAttributesKey? = null) : XValuePresentation() {
    override fun renderValue(renderer: XValueTextRenderer) {
        if (tkey == null) renderer.renderValue(sValue)
        else renderer.renderValue(sValue, tkey)
    }

    override fun getType() = sType
}

class LuaXStringPresentation(sValue: String) : XStringValuePresentation(sValue) {
    override fun getType() = "string"
}

class LuaXNumberPresentation(sValue: String) : XNumericValuePresentation(sValue) {
    override fun getType() = "number"
}

class LuaXBoolPresentation(sValue: String) : LuaXValuePresentation("boolean", sValue, LuaHighlightingData.PRIMITIVE_TYPE)