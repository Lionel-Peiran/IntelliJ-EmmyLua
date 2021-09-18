

package com.tang.intellij.lua.debugger.remote

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.debugger.LuaConfigurationFactory

/**
 *
 * Created by TangZX on 2016/12/30.
 */
class LuaMobConfigurationFactory(type: LuaMobConfigurationType) : LuaConfigurationFactory(type) {

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return LuaMobConfiguration(project, this)
    }
}
