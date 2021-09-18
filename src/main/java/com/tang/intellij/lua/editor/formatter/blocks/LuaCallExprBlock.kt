

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.*
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaCallExpr
import com.tang.intellij.lua.psi.LuaTypes

class LuaCallExprBlock(psi: LuaCallExpr, wrap: Wrap?, alignment: Alignment?, indent: Indent, ctx: LuaFormatContext)
    : LuaScriptBlock(psi, wrap, alignment, indent, ctx) {

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is LuaScriptBlock && child2 is LuaScriptBlock) {
            // call(param)
            return if (child2.node.findChildByType(LuaTypes.LPAREN) != null) {
                Spacing.createSpacing(0, 0, 0, false, 0)
            } else {
                Spacing.createSpacing(1, 1, 0, false, 0)
            }// call "string"
        }
        return super.getSpacing(child1, child2)
    }
}