

package com.tang.intellij.lua.editor.formatter.blocks

import com.intellij.formatting.Alignment
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.editor.formatter.LuaFormatContext
import com.tang.intellij.lua.psi.LuaParenExpr
import com.tang.intellij.lua.psi.LuaTypes

class LuaParenExprBlock(psi: LuaParenExpr, wrap: Wrap?, alignment: Alignment?, indent: Indent, ctx: LuaFormatContext)
    : LuaScriptBlock(psi, wrap, alignment, indent, ctx)  {

    override fun buildChild(child: PsiElement, indent: Indent?): LuaScriptBlock {
        if (child.node.elementType == LuaTypes.LPAREN || child.node.elementType == LuaTypes.RPAREN)
            return super.buildChild(child, indent)
        return super.buildChild(child, Indent.getContinuationIndent())
    }

    override fun getChildAttributes(newChildIndex: Int) =
            ChildAttributes(Indent.getContinuationIndent(), null)
}