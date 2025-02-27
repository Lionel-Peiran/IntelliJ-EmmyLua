

package com.tang.intellij.lua.project

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.tang.intellij.lua.Constants
import com.tang.intellij.lua.lang.LuaLanguageLevel
import java.nio.charset.Charset

/**
 *
 * Created by tangzx on 2017/6/12.
 */
@State(name = "LuaSettings", storages = [(Storage("emmy.xml"))])
class LuaSettings : PersistentStateComponent<LuaSettings> {
    //自定义require函数，参考constructorNames
    var requireLikeFunctionNames: Array<String> = arrayOf("require")

    var constructorNames: Array<String> = arrayOf("new", "get")

    //Doc文档严格模式，对不合法的注解报错
    var isStrictDoc: Boolean = false

    //在未匹配end的statement后回车会自动补全
    // 暂时关闭
    var isSmartCloseEnd: Boolean = true

    //在代码完成时使用参数完成模板
    var autoInsertParameters: Boolean = false

    var isShowWordsInFile: Boolean = true

    // Throw errors if specified and found types do not match
    var isEnforceTypeSafety: Boolean = false

    var isNilStrict: Boolean = false

    var isRecognizeGlobalNameAsType = true

    var additionalSourcesRoot = arrayOf<String>()

    /**
     * 使用泛型
     */
    var enableGeneric: Boolean = false

    /**
     * (KB)
     */
    var tooLargerFileThreshold = 1024

    var attachDebugDefaultCharsetName = "UTF-8"

    var attachDebugCaptureStd = true

    var attachDebugCaptureOutput = true

    /**
     * Lua language level
     */
    var languageLevel = LuaLanguageLevel.LUA54

    override fun getState(): LuaSettings? {
        return this
    }

    override fun loadState(luaSettings: LuaSettings) {
        XmlSerializerUtil.copyBean(luaSettings, this)
    }

    var constructorNamesString: String
        get() {
            return constructorNames.joinToString(";")
        }
        set(value) {
            constructorNames = value.split(";").map { it.trim() }.toTypedArray()
        }

    val attachDebugDefaultCharset: Charset get() {
        return Charset.forName(attachDebugDefaultCharsetName) ?: Charset.forName("UTF-8")
    }
    var requireLikeFunctionNamesString: String
        get() {
            return requireLikeFunctionNames.joinToString(";")
        }
        set(value) {
            requireLikeFunctionNames = value.split(";").map { it.trim() }.toTypedArray()
        }
    companion object {

        val instance: LuaSettings
            get() = ServiceManager.getService(LuaSettings::class.java)

        fun isConstructorName(name: String): Boolean {
            return instance.constructorNames.contains(name)
        }

        fun isRequireLikeFunctionName(name: String): Boolean {
            return instance.requireLikeFunctionNames.contains(name) || name == Constants.WORD_REQUIRE
        }
    }
}
