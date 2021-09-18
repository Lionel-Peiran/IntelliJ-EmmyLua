

package com.tang.intellij.lua.editor.formatter

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import com.intellij.util.DocumentUtil
import com.tang.intellij.lua.psi.LuaPsiFile
import com.tang.intellij.lua.psi.LuaTypes
import com.tang.intellij.lua.psi.LuaVisitor

/**
 * 移除不用的 ;,
 * First Created on 2017/7/8.
 */
class LuaPreFormatProcessor : PreFormatProcessor {
    override fun process(astNode: ASTNode, sourceRange: TextRange): TextRange {
        var range = sourceRange
        val psi = astNode.psi
        if (psi.containingFile is LuaPsiFile) {
            val list = mutableListOf<Pair<Int, Boolean>>()
            psi.accept(object : LuaVisitor() {
                override fun visitElement(o: PsiElement) {
                    if (o.node.elementType == LuaTypes.SEMI) {
                        val nextSibling = o.nextSibling
                        var replaceWithSpace = false
                        if (nextSibling != null) {
                            if (nextSibling.node.startOffset == o.node.startOffset + 1) {
                                replaceWithSpace = true
                            }
                        }
                        list.add(Pair(o.node.startOffset, replaceWithSpace))
                    } else o.acceptChildren(this)
                }
            })

            val psiDocumentManager = PsiDocumentManager.getInstance(psi.project)
            val document = psiDocumentManager.getDocument(psi.containingFile)
            document ?: return range
            DocumentUtil.executeInBulk(document, true) {
                psiDocumentManager.doPostponedOperationsAndUnblockDocument(document)

                var removed = 0
                list.forEach {
                    if (it.second) {
                        document.replaceString(it.first, it.first + 1, " ")
                    } else {
                        document.deleteString(it.first, it.first + 1)
                        removed++
                    }
                }

                psiDocumentManager.commitDocument(document)

                range = TextRange(sourceRange.startOffset, sourceRange.length - removed)
            }

        }
        return range
    }
}