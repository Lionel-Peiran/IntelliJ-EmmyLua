

package com.tang.intellij.lua.hierarchy.call

import com.intellij.ide.hierarchy.CallHierarchyBrowserBase
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor
import com.intellij.ide.hierarchy.HierarchyTreeStructure
import com.intellij.ide.util.treeView.AlphaComparator
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.psi.PsiElement
import com.intellij.ui.PopupHandler
import java.util.*
import javax.swing.JTree

class LuaCallHierarchyBrowser(element: PsiElement) : CallHierarchyBrowserBase(element.project, element) {
    companion object {
        private const val GROUP_LUA_CALL_HIERARCHY_POPUP = "LuaCallHierarchyPopupMenu"
    }

    override fun isApplicableElement(element: PsiElement): Boolean {
        return LuaCallHierarchyUtil.isValidElement(element)
    }

    override fun getComparator(): Comparator<NodeDescriptor<*>>? {
        return AlphaComparator.INSTANCE
    }

    override fun getElementFromDescriptor(descriptor: HierarchyNodeDescriptor): PsiElement? {
        return descriptor.psiElement
    }

    private fun createHierarchyTree(group: ActionGroup): JTree {
        val tree = createTree(false)
        PopupHandler.installPopupHandler(tree, group, ActionPlaces.CALL_HIERARCHY_VIEW_POPUP, ActionManager.getInstance())
        return tree
    }

    override fun createTrees(trees: CreateTreesMap) {
        val group = ActionManager.getInstance().getAction(GROUP_LUA_CALL_HIERARCHY_POPUP) as ActionGroup

        val callerTree = createHierarchyTree(group)
        val calleeTree = createHierarchyTree(group)

        trees[CallHierarchyBrowserBase.CALLER_TYPE] = callerTree
        trees[CallHierarchyBrowserBase.CALLEE_TYPE] = calleeTree
    }

    override fun createHierarchyTreeStructure(typeName: String, psiElement: PsiElement): HierarchyTreeStructure? =
            when (typeName) {
                CallHierarchyBrowserBase.CALLER_TYPE -> LuaCallerFunctionTreeStructure(myProject, psiElement)
                CallHierarchyBrowserBase.CALLEE_TYPE -> LuaCalleeFunctionTreeStructure(myProject, psiElement)
                else -> null
            }
}