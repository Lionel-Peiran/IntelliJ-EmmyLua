

package com.tang.intellij.lua.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * First Created on 2015/11/15.
 * Email:love.tangzx@qq.com
 */
public class LuaFileType extends LanguageFileType {

    public static final LuaFileType INSTANCE = new LuaFileType();

    protected LuaFileType() {
        super(LuaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "lua";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Lua language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "lua";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return LuaIcons.FILE;
    }
}
