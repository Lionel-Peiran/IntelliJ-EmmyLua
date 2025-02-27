

package com.tang.intellij.lua.debugger.remote

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.options.SettingsEditorGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.openapi.util.WriteExternalException
import com.tang.intellij.lua.debugger.IRemoteConfiguration
import com.tang.intellij.lua.debugger.LuaCommandLineState
import com.tang.intellij.lua.debugger.LuaRunConfiguration
import org.jdom.Element

/**
 *
 * Created by TangZX on 2016/12/30.
 */
class LuaMobConfiguration(project: Project, factory: ConfigurationFactory)
    : LuaRunConfiguration(project, factory), IRemoteConfiguration, RunConfigurationWithSuppressedDefaultRunAction {

    override var port = 8172

    override fun checkConfiguration() {
        super.checkConfiguration()
        checkSourceRoot()
    }

    override fun getValidModules(): Collection<Module> {
        //final Module[] modules = ModuleManager.getInstance(getProject()).getModules();
        return emptyList()
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        val group = SettingsEditorGroup<LuaMobConfiguration>()
        group.addEditor("mob", LuaMobSettingsEditor())
        return group
    }

    @Throws(ExecutionException::class)
    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return LuaCommandLineState(executionEnvironment)
    }

    @Throws(WriteExternalException::class)
    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, "PORT", port.toString())
    }

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        super.readExternal(element)
        val port = JDOMExternalizerUtil.readField(element, "PORT")
        if (port != null)
            this.port = Integer.parseInt(port)
    }
}
