

package com.tang.intellij.lua.luacheck

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.options.ShowSettingsUtil
import com.tang.intellij.lua.lang.LuaFileType

class LuaCheckGroup : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(CommonDataKeys.PROJECT)!!
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (file != null) {
            val settings = LuaCheckSettings.getInstance()
            if (settings.valid) {
                runLuaCheck(project, file)
            } else {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, LuaCheckSettingsPanel::class.java)
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val presentation = event.presentation
        val project = event.getData(CommonDataKeys.PROJECT)
        if (project == null) {
            presentation.isEnabled = false
            presentation.isVisible = false
        } else {
            val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
            presentation.isVisible = file != null && (file.isDirectory || file.fileType == LuaFileType.INSTANCE)
            presentation.isEnabled = true
        }
    }
}