

package com.tang.intellij.lua.debugger

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.GenericProgramRunner

/**
 * lua runner
 * First Created on 2017/6/10.
 */
abstract class LuaRunner : GenericProgramRunner<RunnerSettings>() {
    override fun canRun(executorId: String, runProfile: RunProfile): Boolean {
        return DefaultDebugExecutor.EXECUTOR_ID == executorId || DefaultRunExecutor.EXECUTOR_ID == executorId
    }
}
