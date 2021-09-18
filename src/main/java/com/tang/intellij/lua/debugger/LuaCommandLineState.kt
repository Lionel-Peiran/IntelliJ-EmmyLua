

package com.tang.intellij.lua.debugger

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView

/**
 *
 * First Created on 2016/12/30.
 */
class LuaCommandLineState(environment: ExecutionEnvironment) : CommandLineState(environment) {

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val runProfile = environment.runProfile as LuaRunConfiguration
        return ColoredProcessHandler(runProfile.createCommandLine()!!)
    }

    @Throws(ExecutionException::class)
    override fun createConsole(executor: Executor): ConsoleView? {
        val consoleView = super.createConsole(executor)
        consoleView?.addMessageFilter(LuaTracebackFilter(environment.project))
        return consoleView
    }
}
