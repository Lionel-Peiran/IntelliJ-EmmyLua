

package com.tang.intellij.lua.debugger

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.tang.intellij.lua.psi.*

/**
 *
 * First Created on 2017/5/1.
 */
abstract class LuaDebuggerEvaluator : XDebuggerEvaluator() {
    override fun getExpressionRangeAtOffset(project: Project, document: Document, offset: Int, sideEffectsAllowed: Boolean): TextRange? {
        var currentRange: TextRange? = null
        PsiDocumentManager.getInstance(project).commitAndRunReadAction {
            try {
                val file = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return@commitAndRunReadAction
                if (currentRange == null) {
                    val ele = file.findElementAt(offset)
                    if (ele != null && ele.node.elementType == LuaTypes.ID) {
                        val parent = ele.parent
                        when (parent) {
                            is LuaFuncDef,
                            is LuaLocalFuncDef -> currentRange = ele.textRange
                            is LuaClassMethodName,
                            is PsiNameIdentifierOwner -> currentRange = parent.textRange
                        }
                    }
                }

                if (currentRange == null) {
                    val expr = PsiTreeUtil.findElementOfClassAtOffset(file, offset, LuaExpr::class.java, false)
                    currentRange = when (expr) {
                        is LuaCallExpr,
                        is LuaClosureExpr,
                        is LuaLiteralExpr -> null
                        else -> expr?.textRange
                    }
                }
            } catch (ignored: IndexNotReadyException) {
            }
        }
        return currentRange
    }

    override fun evaluate(express: String, xEvaluationCallback: XDebuggerEvaluator.XEvaluationCallback, xSourcePosition: XSourcePosition?) {
        var expr = express.trim()
        if (!expr.endsWith(')')) {
            val lastDot = express.lastIndexOf('.')
            val lastColon = express.lastIndexOf(':')
            if (lastColon > lastDot) // a:xx -> a.xx
                expr = expr.replaceRange(lastColon, lastColon + 1, ".")
        }
        eval(expr, xEvaluationCallback, xSourcePosition)
    }

    protected abstract fun eval(express: String, xEvaluationCallback: XDebuggerEvaluator.XEvaluationCallback, xSourcePosition: XSourcePosition?)
}
