

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.*
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaFuncBody
import com.tang.intellij.lua.psi.LuaParamNameDef
import com.tang.intellij.lua.psi.LuaTypes

class LuaFuncBodyBlock(psi: LuaFuncBody, wrap: Wrap?, alignment: Alignment?, indent: Indent, ctx: LuaFormatContext)
    : LuaIndentBlock(psi, wrap, alignment, indent, ctx) {

    private val paramAlign = Alignment.createAlignment()
    private var paramIndex = 0

    private fun getAlign()
            = if (ctx.settings.ALIGN_MULTILINE_PARAMETERS) paramAlign else null

    override fun buildChild(child: PsiElement, indent: Indent?): LuaScriptBlock {
        if (child is LuaParamNameDef) {
            val wrap = if (paramIndex++ == 0) null else
                Wrap.createWrap(ctx.settings.METHOD_PARAMETERS_WRAP, true)
            return createBlock(child, Indent.getContinuationIndent(), getAlign(), wrap)
        }
        return super.buildChild(child, indent)
    }

    private fun rp(): Int {
        var idx = -1
        childBlocks?.let {
            for (i in 0 until it.size) {
                val child = it[i]
                if (child.elementType == LuaTypes.RPAREN) {
                    idx = i
                    break
                }
            }
        }
        return idx
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return if (newChildIndex <= rp())
            ChildAttributes(Indent.getContinuationIndent(), getAlign())
        else
            ChildAttributes(Indent.getNormalIndent(), null)
    }
}