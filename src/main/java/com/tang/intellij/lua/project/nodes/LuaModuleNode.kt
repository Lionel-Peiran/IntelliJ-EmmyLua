

package com.tang.intellij.lua.project.nodes

import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.ProjectViewModuleNode
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project

class LuaModuleNode(project: Project, value: Module, settings: ViewSettings)
    : ProjectViewModuleNode(project, value, settings) {

    override fun getChildren(): MutableCollection<AbstractTreeNode<*>> {
        val children = super.getChildren()
        for (child in children) {
            if (child is PsiDirectoryNode) {

            }
        }
        return children
    }

}