package com.tang.intellij.lua.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.roots.ModuleRootManager
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.project.LuaSourceRootManager

class MarkLuaSourceRootAction : AnAction(), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (file.isDirectory) {
            ApplicationManager.getApplication().runWriteAction {
                LuaSourceRootManager.getInstance(project).appendRoot(file)
                val module = event.getData(LangDataKeys.MODULE)
                if (module != null) {
                    ModuleRootManager.getInstance(module)?.modifiableModel?.commit()
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = isVisible(e)
        e.presentation.icon = LuaIcons.ROOT
    }

    private fun isVisible(e: AnActionEvent): Boolean {
        val project = e.project ?: return false
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return false
        return !LuaSourceRootManager.getInstance(project).isSourceRoot(file)
    }
}

class UnmarkLuaSourceRootAction : AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (file.isDirectory) {
            ApplicationManager.getApplication().runWriteAction {
                LuaSourceRootManager.getInstance(project).removeRoot(file)
                val module = event.getData(LangDataKeys.MODULE)
                if (module != null) {
                    ModuleRootManager.getInstance(module)?.modifiableModel?.commit()
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = isVisible(e)
        e.presentation.icon = LuaIcons.ROOT
    }

    private fun isVisible(e: AnActionEvent): Boolean {
        val project = e.project ?: return false
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return false
        return LuaSourceRootManager.getInstance(project).isSourceRoot(file)
    }
}