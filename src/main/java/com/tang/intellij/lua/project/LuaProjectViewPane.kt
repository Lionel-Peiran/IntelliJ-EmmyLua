

package com.tang.intellij.lua.project

import com.intellij.ide.SelectInTarget
import com.intellij.ide.impl.ProjectViewSelectInTarget
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.AbstractProjectViewPSIPane
import com.intellij.ide.projectView.impl.ProjectAbstractTreeStructureBase
import com.intellij.ide.projectView.impl.ProjectTreeStructure
import com.intellij.ide.projectView.impl.ProjectViewTree
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeBuilder
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.AbstractTreeUpdater
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.lang.LuaFileType
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.project.nodes.LuaProjectRootNode
import com.tang.intellij.lua.project.nodes.LuaPsiDirectoryNode
import javax.swing.Icon
import javax.swing.tree.DefaultTreeModel

class LuaProjectViewPane(project: Project) : AbstractProjectViewPSIPane(project), Disposable {
    private val connection = project.messageBus.connect()

    init {
        connection.subscribe(LuaSourceRootManager.TOPIC, object : LuaSourceRootListener {
            override fun onChanged() {
                this@LuaProjectViewPane.updateFromRoot(false)
            }
        })
    }

    companion object {
        const val ID = "lua.project.view"
    }

    override fun getId() = ID

    override fun createTreeUpdater(builder: AbstractTreeBuilder): AbstractTreeUpdater {
        return object : AbstractTreeUpdater(builder) {

        }
    }

    override fun getTitle(): String {
        return "EmmyLua Explorer"
    }

    override fun createSelectInTarget(): SelectInTarget {
        return object : ProjectViewSelectInTarget(myProject) {
            override fun toString(): String {
                return title
            }

            override fun getMinorViewId(): String {
                return id
            }
        }
    }

    override fun createStructure(): ProjectAbstractTreeStructureBase {
        return object : ProjectTreeStructure(myProject, id) {
            override fun createRoot(project: Project, settings: ViewSettings): AbstractTreeNode<*> {
                return LuaProjectRootNode(project, settings)
            }

            override fun getProviders(): MutableList<TreeStructureProvider> {
                return mutableListOf(LuaTreeStructureProvider())
            }
        }
    }

    override fun getIcon(): Icon {
        return LuaIcons.PROJECT
    }

    override fun getWeight(): Int {
        return 10
    }

    override fun createTree(model: DefaultTreeModel): ProjectViewTree {
        return LuaProjectTreeView(model)
    }

    private inner class LuaProjectTreeView(model: DefaultTreeModel) : ProjectViewTree(myProject, model)
}

private class LuaTreeStructureProvider : TreeStructureProvider {
    override fun modify(parent: AbstractTreeNode<*>, list: MutableCollection<AbstractTreeNode<*>>, settings: ViewSettings): MutableCollection<AbstractTreeNode<*>> {
        val newChildren = mutableListOf<AbstractTreeNode<*>>()
        for (node in list) {
            if (node is PsiFileNode) {
                if (node.virtualFile?.fileType == LuaFileType.INSTANCE) {
                    newChildren.add(node)
                }
            } else if (node is PsiDirectoryNode) {
                newChildren.add(LuaPsiDirectoryNode(node.project!!, node.value, settings))
            } else {
                newChildren.add(node)
            }
        }
        return newChildren
    }
}