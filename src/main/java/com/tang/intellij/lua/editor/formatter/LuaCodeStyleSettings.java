

package com.tang.intellij.lua.editor.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.tang.intellij.lua.lang.LuaLanguage;

/**
 * LuaCodeStyleSettings
 * First Created on 2017/2/22.
 */
public class LuaCodeStyleSettings extends CustomCodeStyleSettings {

    public boolean SPACE_AFTER_TABLE_FIELD_SEP = true;
    public boolean SPACE_AROUND_BINARY_OPERATOR = true;
    public boolean SPACE_INSIDE_INLINE_TABLE = true;

    public boolean ALIGN_TABLE_FIELD_ASSIGN = false;

    LuaCodeStyleSettings(CodeStyleSettings container) {
        super(LuaLanguage.INSTANCE.getID(), container);
    }
}
