

package com.tang.intellij.lua.editor.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.tang.intellij.lua.lang.LuaLanguage

/**

 * First Created on 2017/2/22.
 */
class LuaCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings): Configurable {
        return object : CodeStyleAbstractConfigurable(settings, originalSettings, "EmmyLua") {

            override fun getHelpTopic() = "reference.settingsdialog.codestyle.lua"

            override fun createPanel(codeStyleSettings: CodeStyleSettings): CodeStyleAbstractPanel {
                val language = LuaLanguage.INSTANCE
                val currentSettings = currentSettings
                return object : TabbedLanguageCodeStylePanel(language, currentSettings, codeStyleSettings) {
                    override fun initTabs(styleSettings: CodeStyleSettings) {
                        //super.initTabs(styleSettings);
                        addIndentOptionsTab(styleSettings)
                        addSpacesTab(styleSettings)
                        //addBlankLinesTab(styleSettings)
                        addWrappingAndBracesTab(styleSettings)
                    }
                }
            }
        }
    }

    override fun getConfigurableDisplayName() = LuaLanguage.INSTANCE.displayName

    override fun createCustomSettings(settings: CodeStyleSettings?): CustomCodeStyleSettings? {
        return LuaCodeStyleSettings(settings)
    }
}
