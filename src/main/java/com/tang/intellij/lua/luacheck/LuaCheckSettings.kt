

package com.tang.intellij.lua.luacheck

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "LuaCheckSettings", storages = [Storage("emmy.xml")])
class LuaCheckSettings : PersistentStateComponent<LuaCheckSettings> {
    var luaCheck:String? = null
    var luaCheckArgs:String? = null

    override fun getState(): LuaCheckSettings = this

    override fun loadState(settings: LuaCheckSettings) {
        XmlSerializerUtil.copyBean(settings, this)
    }

    val valid: Boolean get() {
        if (StringUtil.isEmpty(luaCheck))
            return false

        return true
    }

    companion object {
        @JvmStatic fun getInstance(): LuaCheckSettings {
            return ServiceManager.getService(LuaCheckSettings::class.java)
        }
    }
}
