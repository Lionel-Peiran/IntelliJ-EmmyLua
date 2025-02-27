

package com.tang.intellij.lua.debugger

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.debugger.app.LuaAppConfigurationType
import com.tang.intellij.lua.debugger.app.LuaAppRunConfiguration
import com.tang.intellij.lua.lang.LuaFileType
import com.tang.intellij.lua.project.LuaSourceRootManager
import com.tang.intellij.lua.psi.LuaFileUtil
import com.tang.intellij.lua.psi.LuaPsiFile

/**
 * Supports creating run configurations from context (by right-clicking a code element in the source editor or the project view).
 * First Created on 2017/6/3.
 */
class LuaRunConfigurationProducer : RunConfigurationProducer<LuaAppRunConfiguration>(LuaAppConfigurationType.getInstance()) {

    override fun setupConfigurationFromContext(luaAppRunConfiguration: LuaAppRunConfiguration, configurationContext: ConfigurationContext, ref: Ref<PsiElement>): Boolean {
        val element = ref.get()
        val containingFile = element.containingFile as? LuaPsiFile ?: return false

        luaAppRunConfiguration.debuggerType = DebuggerType.Mob
        luaAppRunConfiguration.file = LuaFileUtil.getShortPath(element.project, containingFile.virtualFile)
        luaAppRunConfiguration.name = containingFile.name

        val dir = containingFile.parent?.virtualFile
        val module = configurationContext.module
        if (dir != null && module != null) {
            val rootManager = LuaSourceRootManager.getInstance(element.project)
            for (root in rootManager.getSourceRoots()) {
                if (root.url.startsWith(dir.url)) {
                    luaAppRunConfiguration.workingDir = root.canonicalPath
                    break
                }
            }
        }
        return true
    }

    override fun isConfigurationFromContext(luaAppRunConfiguration: LuaAppRunConfiguration, configurationContext: ConfigurationContext): Boolean {
        val element = configurationContext.psiLocation ?: return false
        val psiFile = element.containingFile
        if (psiFile == null || psiFile.fileType !== LuaFileType.INSTANCE)
            return false
        val file = luaAppRunConfiguration.virtualFile
        return psiFile.virtualFile == file
    }
}
