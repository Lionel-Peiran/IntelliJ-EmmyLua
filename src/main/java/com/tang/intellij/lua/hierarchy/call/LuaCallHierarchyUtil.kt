

package com.tang.intellij.lua.hierarchy.call

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.searches.ReferencesSearch
import com.tang.intellij.lua.psi.*

object LuaCallHierarchyUtil {
    fun isValidElement(element: PsiElement?): Boolean {
        return element is LuaClassMethodDef || element is LuaFuncDef || element is LuaLocalFuncDef
    }

    fun getValidParentElement(element: PsiElement?): PsiElement? {
        var curElement = element
        while (curElement != null) {
            if (isValidElement(curElement)) return curElement
            if (curElement is PsiFile) break
            curElement = curElement.parent
        }
        return null
    }

    fun getCallers(element: PsiElement): List<PsiElement> {
        return ReferencesSearch.search(element)
                .mapNotNull { LuaCallHierarchyUtil.getValidParentElement(it.element) }
    }

    fun getCallees(element: PsiElement): List<PsiElement> {
        val callees = mutableListOf<PsiElement>()
        val visitor = object : LuaRecursiveVisitor() {
            override fun visitCallExpr(o: LuaCallExpr) {
                o.expr.reference?.resolve()?.takeIf { LuaCallHierarchyUtil.isValidElement(it) }?.let {
                    callees.add(it)
                }
            }
        }
        visitor.visitElement(element)
        return callees
    }

}