

package com.tang.intellij.lua.hierarchy.call

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class LuaCalleeFunctionTreeStructure(project: Project, element: PsiElement) : LuaCallHierarchyTreeStructureBase(project, element) {
    override fun getChildren(element: PsiElement): List<PsiElement> {
        return LuaCallHierarchyUtil.getCallees(element)
    }

}