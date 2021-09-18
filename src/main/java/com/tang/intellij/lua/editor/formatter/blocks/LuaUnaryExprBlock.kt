

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.*
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaTypes.NOT
import com.tang.intellij.lua.psi.LuaUnaryExpr

/**
 *
 * First Created on 2017/4/23.
 */
class LuaUnaryExprBlock internal constructor(psi: LuaUnaryExpr,
                                             wrap: Wrap?,
                                             alignment: Alignment?,
                                             indent: Indent,
                                             ctx: LuaFormatContext)
    : LuaScriptBlock(psi, wrap, alignment, indent, ctx) {

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val c1 = child1 as LuaScriptBlock
        return if (c1.node.findChildByType(NOT) != null) Spacing.createSpacing(1, 1, 0, true, 1) else super.getSpacing(child1, child2)
    }
}
