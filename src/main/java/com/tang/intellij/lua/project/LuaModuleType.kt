

package com.tang.intellij.lua.project

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.tang.intellij.lua.lang.LuaIcons
import org.jetbrains.jps.model.module.JpsModuleSourceRootType
import javax.swing.Icon

/**
 *
 * Created by tangzx on 2016/12/24.
 */
class LuaModuleType : ModuleType<LuaModuleBuilder>(MODULE_TYPE) {

    // IDEA-171
    val bigIcon: Icon
        get() = LuaIcons.MODULE

    override fun createModuleBuilder() = LuaModuleBuilder()

    override fun getName() = "Lua"

    override fun getDescription() = "Lua module"

    override fun getNodeIcon(b: Boolean): Icon = LuaIcons.MODULE

    override fun isMarkInnerSupportedFor(type: JpsModuleSourceRootType<*>?) = true

    companion object {
        const val MODULE_TYPE = "LUA_MODULE"

        val instance: LuaModuleType
            get() = ModuleTypeManager.getInstance().findByID(MODULE_TYPE) as LuaModuleType
    }
}
