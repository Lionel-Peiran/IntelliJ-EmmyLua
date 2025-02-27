

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.impl.TextExpression
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.codeInsight.template.macro.SuggestLuaParametersMacro
import com.tang.intellij.lua.psi.LuaClosureExpr
import com.tang.intellij.lua.psi.LuaIndentRange
import com.tang.intellij.lua.psi.LuaTypes.*

/**
 * 关键字插入时缩进处理
 * First Created on 2016/12/20.
 */
class KeywordInsertHandler internal constructor(private val keyWordToken: IElementType) : InsertHandler<LookupElement> {

    override fun handleInsert(insertionContext: InsertionContext, lookupElement: LookupElement) {
        val file = insertionContext.file
        val project = insertionContext.project
        val document = insertionContext.document
        val offset = insertionContext.tailOffset
        if (keyWordToken == FUNCTION) {
            val element = file.findElementAt(insertionContext.startOffset)
            if (element?.parent is LuaClosureExpr) {
                val templateManager = TemplateManager.getInstance(project)
                val template = templateManager.createTemplate("", "", "(\$PARAMETERS\$) \$END\$ end")
                template.addVariable("PARAMETERS", MacroCallNode(SuggestLuaParametersMacro(SuggestLuaParametersMacro.Position.KeywordInsertHandler)), TextExpression(""), false)
                templateManager.startTemplate(insertionContext.editor, template)
            }
        } else {
            val element = file.findElementAt(offset)
            if (element != null && element !is PsiWhiteSpace) {
                document.insertString(insertionContext.tailOffset, " ")
                insertionContext.editor.caretModel.moveToOffset(insertionContext.tailOffset)
            }
        }
        autoIndent(keyWordToken, file, project, document, offset)
    }

    companion object {

        fun autoIndent(keyWordToken: IElementType, file: PsiFile, project: Project, document: Document, offset: Int) {
            if (keyWordToken === END || keyWordToken === ELSE || keyWordToken === ELSEIF) {
                PsiDocumentManager.getInstance(project).commitDocument(document)
                val element = PsiTreeUtil.findElementOfClassAtOffset(file, offset, LuaIndentRange::class.java, false)
                if (element != null) {
                    val styleManager = CodeStyleManager.getInstance(project)
                    styleManager.adjustLineIndent(file, element.textRange)
                }
            }
        }
    }
}
