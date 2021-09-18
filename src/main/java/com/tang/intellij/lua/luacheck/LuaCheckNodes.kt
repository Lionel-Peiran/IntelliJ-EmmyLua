

package com.tang.intellij.lua.luacheck

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class LCRootNode(project: Project) : AbstractTreeNode<Any>(project, project) {
    private val myChildren: ArrayList<AbstractTreeNode<*>> = arrayListOf()
    override fun getChildren() = myChildren

    override fun update(data: PresentationData) {

    }

    fun clear() {
        myChildren.clear()
    }

    fun append(child: AbstractTreeNode<*>) {
        myChildren.add(child)
    }
}

class LCPsiFileNode(project: Project, file: PsiFile) : PsiFileNode(project, file, ViewSettings.DEFAULT) {
    private val myChildren: ArrayList<AbstractTreeNode<*>> = arrayListOf()

    override fun getChildrenImpl() = myChildren

    fun append(child: AbstractTreeNode<*>) {
        myChildren.add(child)
    }
}

data class LCRecordData(val line:Int, val col:Int, val len:Int, val desc:String)
class LCRecord(project: Project, val file:PsiFile, val record: LCRecordData) : AbstractTreeNode<LCRecordData>(project, record) {
    override fun update(presentationData: PresentationData) {
        presentationData.presentableText = "(${record.line}, ${record.col}) ${record.desc}"
        presentationData.setIcon(AllIcons.General.TodoDefault)
    }

    override fun getChildren(): Collection<AbstractTreeNode<Any>> {
        return emptyList()
    }

    override fun canNavigate() = true

    override fun navigate(requestFocus: Boolean) {
        getNavigator().navigate(requestFocus)
    }

    fun getNavigator():OpenFileDescriptor  {
        return OpenFileDescriptor(project!!, file.virtualFile, record.line, record.col)
    }

    override fun isAlwaysLeaf() = true
}