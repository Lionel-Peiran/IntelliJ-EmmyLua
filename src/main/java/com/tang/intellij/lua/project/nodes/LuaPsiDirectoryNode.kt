

package com.tang.intellij.lua.project.nodes

import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class LuaPsiDirectoryNode(project: Project, value: PsiDirectory, settings: ViewSettings)
    : PsiDirectoryNode(project, value, settings) {
    /*override fun setupIcon(data: PresentationData, psiDirectory: PsiDirectory) {
        if (LuaSourceRootManager.getInstance(psiDirectory.project).isSourceRoot(psiDirectory.virtualFile)) {
            data.setIcon(PlatformIcons.MODULES_SOURCE_FOLDERS_ICON)
            return
        }
        super.setupIcon(data, psiDirectory)
    }*/
}