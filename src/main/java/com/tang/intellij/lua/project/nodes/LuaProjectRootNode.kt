

package com.tang.intellij.lua.project.nodes

import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.ExternalLibrariesNode
import com.intellij.ide.projectView.impl.nodes.ProjectViewProjectNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project

class LuaProjectRootNode(project: Project, viewSettings: ViewSettings) : ProjectViewProjectNode(project, viewSettings) {
    override fun getChildren(): MutableCollection<AbstractTreeNode<*>> {
        if (myProject.isDisposed)
            return mutableListOf()
        val list = mutableListOf<AbstractTreeNode<*>>()

        val modules = ModuleManager.getInstance(myProject).modules
        for (module in modules) {
            list.add(LuaModuleNode(myProject, module, settings))
        }

        list.add(ExternalLibrariesNode(myProject, settings))
        return list
    }
}