

package com.tang.intellij.lua.luacheck

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory

/**
 * LuaCheckView
 * Created by tangzx on 2017/7/12.
 */
class LuaCheckView(val project: Project) {
    val panel: LuaCheckPanel by lazy { LuaCheckPanel(project) }

    fun init(toolWindow: ToolWindow) {
        panel.init()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}