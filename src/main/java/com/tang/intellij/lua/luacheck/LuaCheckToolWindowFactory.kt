

package com.tang.intellij.lua.luacheck

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

/**
 * ToolWindowFactory
 * First Created on 2017/7/11.
 */
class LuaCheckToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        DumbService.getInstance(project).runWhenSmart {
            val checkView = ServiceManager.getService(project, LuaCheckView::class.java)
            checkView.init(toolWindow)
        }
    }
}