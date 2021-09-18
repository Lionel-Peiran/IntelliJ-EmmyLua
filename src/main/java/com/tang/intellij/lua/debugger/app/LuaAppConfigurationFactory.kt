

package com.tang.intellij.lua.debugger.app

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.debugger.LuaConfigurationFactory

/**
 *
 * Created by tangzx on 2017/5/7.
 */
class LuaAppConfigurationFactory(type: ConfigurationType) : LuaConfigurationFactory(type) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return LuaAppRunConfiguration(project, this)
    }
}
