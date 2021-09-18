

package com.tang.intellij.lua.debugger

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.util.Key

/**
 * base configuration factory
 * disable `Make` task
 */
abstract class LuaConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun configureBeforeRunTaskDefaults(providerID: Key<out BeforeRunTask<*>>?, task: BeforeRunTask<*>?) {
        if ("Make" == providerID?.toString())
            task?.isEnabled = false
    }

    override fun getId(): String {
        return name
    }
}