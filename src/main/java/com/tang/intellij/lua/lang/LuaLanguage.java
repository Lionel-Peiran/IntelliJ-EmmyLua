

package com.tang.intellij.lua.lang;

import com.intellij.lang.Language;

/**
 * First Created on 2015/11/15.
 * Email:love.tangzx@qq.com
 */
public class LuaLanguage extends Language {

    public static final int INDEX_VERSION = 38;

    public static final LuaLanguage INSTANCE = new LuaLanguage();

    public LuaLanguage() {
        super("Lua");
    }
}
