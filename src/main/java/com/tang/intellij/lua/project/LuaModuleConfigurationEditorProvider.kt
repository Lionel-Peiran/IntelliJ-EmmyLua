

package com.tang.intellij.lua.project

import com.intellij.openapi.module.ModuleConfigurationEditor
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState
import java.util.*

class LuaModuleConfigurationEditorProvider : ModuleConfigurationEditorProvider {
    override fun createEditors(moduleConfigurationState: ModuleConfigurationState): Array<ModuleConfigurationEditor> {
        val editors = ArrayList<ModuleConfigurationEditor>()
        val module = moduleConfigurationState.rootModel.module
        val moduleType = ModuleType.get(module)
        if (moduleType == LuaModuleType.instance) {
            val clazz = Class.forName("com.intellij.openapi.roots.ui.configuration.DefaultModuleConfigurationEditorFactory")
            if (clazz != null) {
                val getInstance = clazz.getMethod("getInstance")
                val factory = getInstance.invoke(clazz)
                val createModuleContentRootsEditor = clazz.getMethod("createModuleContentRootsEditor", ModuleConfigurationState::class.java)
                val editor1 = createModuleContentRootsEditor.invoke(factory, moduleConfigurationState) as ModuleConfigurationEditor
                val createClasspathEditor = clazz.getMethod("createClasspathEditor", ModuleConfigurationState::class.java)
                val editor2 = createClasspathEditor.invoke(factory, moduleConfigurationState) as ModuleConfigurationEditor
                editors.add(editor1)
                editors.add(editor2)
            }
        }
        return editors.toTypedArray()
    }
}
