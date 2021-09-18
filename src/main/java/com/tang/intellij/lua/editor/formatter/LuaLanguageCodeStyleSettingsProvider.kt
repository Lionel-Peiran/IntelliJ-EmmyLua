

package com.tang.intellij.lua.editor.formatter

import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.SPACES_OTHER
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.tang.intellij.lua.lang.LuaLanguage

/**

 * First Created on 2017/2/22.
 */
class LuaLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): LuaLanguage = LuaLanguage.INSTANCE

    override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType): String {
        return CodeStyleAbstractPanel.readFromFile(this.javaClass, "preview.lua.template")
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings {
        val commonSettings = CommonCodeStyleSettings(language)
        commonSettings.initIndentOptions()
        return commonSettings
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: LanguageCodeStyleSettingsProvider.SettingsType) {
        when (settingsType) {
            LanguageCodeStyleSettingsProvider.SettingsType.SPACING_SETTINGS -> {
                consumer.showCustomOption(LuaCodeStyleSettings::class.java, "SPACE_AFTER_TABLE_FIELD_SEP", "After field sep", SPACES_OTHER)
                consumer.showCustomOption(LuaCodeStyleSettings::class.java, "SPACE_AROUND_BINARY_OPERATOR", "Around binary operator", SPACES_OTHER)
                consumer.showCustomOption(LuaCodeStyleSettings::class.java, "SPACE_INSIDE_INLINE_TABLE", "Inside inline table", SPACES_OTHER)
                consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS",
                        "SPACE_BEFORE_COMMA",
                        "SPACE_AFTER_COMMA")
            }
            LanguageCodeStyleSettingsProvider.SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                        "METHOD_PARAMETERS_WRAP",
                        "ALIGN_MULTILINE_PARAMETERS",

                        "CALL_PARAMETERS_WRAP",
                        "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",

                        // keep when reformatting
                        "KEEP_SIMPLE_BLOCKS_IN_ONE_LINE",

                        //align group declarations
                        "ALIGN_CONSECUTIVE_VARIABLE_DECLARATIONS"
                )

                consumer.showCustomOption(LuaCodeStyleSettings::class.java,
                        "ALIGN_TABLE_FIELD_ASSIGN",
                        "Align table field assign",
                        "Table")
            }
            else -> {
            }
        }
    }
}
