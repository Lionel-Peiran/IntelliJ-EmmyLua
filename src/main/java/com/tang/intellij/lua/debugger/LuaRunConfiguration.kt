

package com.tang.intellij.lua.debugger

import com.intellij.execution.configuration.AbstractRunConfiguration
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager

/**
 *
 * First Created on 2017/6/4.
 */
abstract class LuaRunConfiguration(project: Project, factory: ConfigurationFactory) : AbstractRunConfiguration(project, factory) {
    @Throws(RuntimeConfigurationException::class)
    protected fun checkSourceRoot() {
        var sourceRootExist = false
        val modules = ModuleManager.getInstance(project).modules
        for (module in modules) {
            val sourceRoots = ModuleRootManager.getInstance(module).sourceRoots
            if (sourceRoots.isNotEmpty()) {
                sourceRootExist = true
                break
            }
        }

        if (!sourceRootExist) {
            throw RuntimeConfigurationError("Sources root not found.")
        }
    }

    open fun createCommandLine(): GeneralCommandLine? {
        return null
    }
}
