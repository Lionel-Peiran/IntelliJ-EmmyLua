

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.ty.IFunSignature
import com.tang.intellij.lua.ty.hasVarargs
import com.tang.intellij.lua.ty.processArgs

open class SignatureInsertHandler(val sig: IFunSignature, private val isColonStyle: Boolean = false) : ArgsInsertHandler() {

    private val myParams: Array<LuaParamInfo> by lazy {
        val list = mutableListOf<LuaParamInfo>()
        sig.processArgs(null, isColonStyle) { _, param ->
            list.add(param)
        }
        list.toTypedArray()
    }

    override fun getParams(): Array<LuaParamInfo> = myParams

    override val isVarargs: Boolean
        get() = sig.hasVarargs()
}

/**
 * "string":sub() -> ("string"):sub()
 */
class SignatureInsertHandlerForString(sig: IFunSignature,
                                      isColonStyle: Boolean = false) : SignatureInsertHandler(sig, isColonStyle) {
    override fun appendSignature(insertionContext: InsertionContext, editor: Editor, element: PsiElement?) {
        val startOffset = insertionContext.startOffset
        val indexExpr = insertionContext.file.findElementAt(startOffset)?.parent as? LuaIndexExpr
        if (indexExpr != null) {
            val prefixExpr = indexExpr.prefixExpr
            if (prefixExpr is LuaLiteralExpr && prefixExpr.kind == LuaLiteralKind.String) {
                val node = prefixExpr.node
                insertionContext.document.insertString(node.startOffset + node.textLength, ")")
                insertionContext.document.insertString(node.startOffset, "(")

                insertionContext.offsetMap.addOffset(CompletionInitializationContext.START_OFFSET, 2)
            }
        }

        super.appendSignature(insertionContext, editor, element)
    }
}