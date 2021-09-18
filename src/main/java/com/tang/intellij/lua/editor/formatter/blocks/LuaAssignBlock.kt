

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaTypes

class LuaAssignBlock(psi: PsiElement, wrap: Wrap?, alignment: Alignment?, indent: Indent, ctx: LuaFormatContext)
    : LuaScriptBlock(psi, wrap, alignment, indent, ctx)  {

    private val _assignAlign = Alignment.createAlignment(true)

    private val assignAlign:Alignment? get() {
        val prev = getPrevSkipComment()
        return if (prev is LuaAssignBlock) prev.assignAlign else _assignAlign
    }

    override fun buildChild(child: PsiElement, indent: Indent?): LuaScriptBlock {
        if (ctx.settings.ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS && child.node.elementType == LuaTypes.ASSIGN) {
            return createBlock(child, Indent.getContinuationIndent(), assignAlign)
        }
        return super.buildChild(child, indent)
    }
}