

package com.tang.intellij.lua.hierarchy.call

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor
import com.intellij.ide.hierarchy.HierarchyTreeStructure
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

abstract class LuaCallHierarchyTreeStructureBase(
        project: Project,
        element: PsiElement
) : HierarchyTreeStructure(project, LuaHierarchyNodeDescriptor(null, element, true)) {

    protected abstract fun getChildren(element: PsiElement): List<PsiElement>

    override fun buildChildren(descriptor: HierarchyNodeDescriptor): Array<Any> {
        if (descriptor is LuaHierarchyNodeDescriptor) {
            val element = descriptor.psiElement
            val isCallable = LuaCallHierarchyUtil.isValidElement(element)
            val nodeDescriptor = baseDescriptor
            if (element == null || !isCallable || nodeDescriptor == null) {
                return emptyArray()
            }

            val children = getChildren(element)
            return children.distinct().map{LuaHierarchyNodeDescriptor(descriptor, it, false)}.toTypedArray()
        }
        return emptyArray()
    }

    override fun isAlwaysShowPlus(): Boolean {
        return true
    }
}