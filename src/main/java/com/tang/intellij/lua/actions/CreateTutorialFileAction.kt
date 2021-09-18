package com.tang.intellij.lua.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.tang.intellij.lua.lang.LuaIcons

class CreateTutorialFileAction : CreateFileFromTemplateAction("CreateTutorialFile", "", LuaIcons.FILE) {
    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return "EmmyDoc Tutorial"
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("EmmyDoc Tutorial").addKind("Source File", LuaIcons.FILE, "Tutorial.lua")
    }
}