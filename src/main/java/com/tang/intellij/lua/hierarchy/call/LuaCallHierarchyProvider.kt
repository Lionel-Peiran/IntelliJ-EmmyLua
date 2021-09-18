

package com.tang.intellij.lua.hierarchy.call

import com.intellij.ide.hierarchy.CallHierarchyBrowserBase
import com.intellij.ide.hierarchy.HierarchyBrowser
import com.intellij.ide.hierarchy.HierarchyProvider
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.psi.PsiElement

class LuaCallHierarchyProvider : HierarchyProvider {
    override fun getTarget(dataContext: DataContext): PsiElement? {
        val editor = dataContext.getData(CommonDataKeys.EDITOR) ?: return null
        val psiFile = dataContext.getData(CommonDataKeys.PSI_FILE) ?: return null
        val element = psiFile.findElementAt(editor.caretModel.offset)
        return LuaCallHierarchyUtil.getValidParentElement(element)
    }

    override fun createHierarchyBrowser(target: PsiElement): HierarchyBrowser {
        return LuaCallHierarchyBrowser(target)
    }

    override fun browserActivated(hierarchyBrowser: HierarchyBrowser) {
        (hierarchyBrowser as LuaCallHierarchyBrowser).changeView(CallHierarchyBrowserBase.CALLER_TYPE)
    }
}