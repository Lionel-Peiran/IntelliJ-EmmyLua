

package com.tang.intellij.lua.project

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.DirectoryProjectGeneratorBase
import com.tang.intellij.lua.lang.LuaIcons
import javax.swing.Icon

class EmmyLuaDirectoryProjectGenerator : DirectoryProjectGeneratorBase<Any>() {
    override fun generateProject(project: Project, file: VirtualFile, p2: Any, module: Module) {

    }

    override fun getName(): String {
        return "EmmyLua"
    }

    override fun getLogo(): Icon = LuaIcons.FILE
}