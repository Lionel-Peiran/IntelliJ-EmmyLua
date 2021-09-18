

package com.tang.intellij.lua.editor.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.tang.intellij.lua.editor.formatter.blocks.LuaScriptBlock

class LuaFormatContext(val settings: CommonCodeStyleSettings, val luaSettings: LuaCodeStyleSettings, val spaceBuilder: SpacingBuilder) {
    var eqAlignment: Alignment? = null
    var lastBlock: LuaScriptBlock? = null
}