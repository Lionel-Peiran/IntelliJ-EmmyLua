

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.Alignment
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaExpr
import com.tang.intellij.lua.psi.LuaListArgs

class LuaListArgsBlock(psi: LuaListArgs, wrap: Wrap?, alignment: Alignment?, indent: Indent, ctx: LuaFormatContext)
    : LuaScriptBlock(psi, wrap, alignment, indent, ctx) {

    private val argAlign = Alignment.createAlignment()
    private var argIndex = 0

    private fun getAlign()
            = if (ctx.settings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS) argAlign else null

    override fun buildChild(child: PsiElement, indent: Indent?): LuaScriptBlock {
        if (child is LuaExpr) {
            val wrap = if (argIndex++ == 0) null else
                Wrap.createWrap(ctx.settings.CALL_PARAMETERS_WRAP, true)
            return createBlock(child, Indent.getContinuationIndent(), getAlign(), wrap)
        }
        return super.buildChild(child, indent)
    }

    override fun getChildAttributes(newChildIndex: Int) =
            ChildAttributes(Indent.getContinuationIndent(), getAlign())
}