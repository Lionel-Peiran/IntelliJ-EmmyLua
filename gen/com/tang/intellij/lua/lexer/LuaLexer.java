

package com.tang.intellij.lua.lexer;

import com.intellij.lexer.FlexAdapter;
import com.tang.intellij.lua.lang.LuaLanguageLevel;

public class LuaLexer extends FlexAdapter {
    public LuaLexer(LuaLanguageLevel level) {
        super(new _LuaLexer(level));
    }
}
