

package com.tang.intellij.lua.lexer;

import com.intellij.lexer.FlexAdapter;

public class LuaRegionLexer extends FlexAdapter {
    public LuaRegionLexer() {
        super(new _LuaRegionLexer());
    }
}
