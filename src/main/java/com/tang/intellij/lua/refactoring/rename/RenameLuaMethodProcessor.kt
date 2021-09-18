

package com.tang.intellij.lua.refactoring.rename

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.util.MergeQuery
import com.tang.intellij.lua.psi.LuaClassMethod
import com.tang.intellij.lua.psi.search.LuaOverridenMethodsSearch
import com.tang.intellij.lua.psi.search.LuaOverridingMethodsSearch

/**
 *
 * Created by tangzx on 2017/3/29.
 */
class RenameLuaMethodProcessor : RenamePsiElementProcessor() {
    override fun canProcessElement(psiElement: PsiElement): Boolean {
        return psiElement is LuaClassMethod
    }

    override fun prepareRenaming(element: PsiElement, newName: String, allRenames: MutableMap<PsiElement, String>, scope: SearchScope) {
        val methodDef = element as LuaClassMethod

        /**
         * bug fix #167
         * TODO: optimize this issue solution
         * FIXME: the main reason is that `LuaNameExpr.infer` is suspended by `recursionGuard`
         * suspended stack : rename -> ... -> LuaNameExpr.infer -> ... -> rebuild stub index -> ... -> LuaNameExpr.infer (suspended)
         */
        FileDocumentManager.getInstance().saveAllDocuments()

        val search = MergeQuery(LuaOverridingMethodsSearch.search(methodDef), LuaOverridenMethodsSearch.search(methodDef))
        search.forEach {
            allRenames[it] = newName

            ReferencesSearch.search(it, scope).forEach { ref ->
                allRenames[ref.element] = newName
            }
        }
    }
}
