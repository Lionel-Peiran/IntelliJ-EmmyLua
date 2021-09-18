

package com.tang.intellij.lua.refactoring

import com.intellij.codeInsight.PsiEquivalenceUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.util.text.StringUtil.isJavaIdentifierPart
import com.intellij.openapi.util.text.StringUtil.isJavaIdentifierStart
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.psi.LuaVisitor
import java.util.*

/**
 * RefactoringUtil
 * Created by tangzx on 2017/4/30.
 */
object LuaRefactoringUtil {
    fun getOccurrences(pattern: PsiElement, context: PsiElement?): List<PsiElement> {
        if (context == null) {
            return emptyList()
        }
        val occurrences = ArrayList<PsiElement>()
        val visitor = object : LuaVisitor() {
            override fun visitElement(element: PsiElement) {
                if (PsiEquivalenceUtil.areElementsEquivalent(element, pattern)) {
                    occurrences.add(element)
                    return
                }
                element.acceptChildren(this)
            }
        }
        context.acceptChildren(visitor)
        return occurrences
    }

    fun isLuaIdentifier(name: String): Boolean {
        return StringUtil.isJavaIdentifier(name)
    }

    fun isLuaTypeIdentifier(text: String): Boolean {
        val len = text.length
        if (len == 0) {
            return false
        } else if (!isJavaIdentifierStart(text[0])) {
            return false
        } else {
            for (i in 1 until len) {
                val c = text[i]
                if (!isJavaIdentifierPart(c) && c != '.') {
                    return false
                }
            }
            return true
        }
    }
}
