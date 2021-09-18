

package com.tang.intellij.lua.psi;

import com.intellij.psi.tree.IElementType;
import com.tang.intellij.lua.lang.LuaLanguage;

/**
 *
 * First Created on 2017/2/26.
 */
public interface LuaStringTypes {
    IElementType NEXT_LINE = new IElementType("NEXT_LINE", LuaLanguage.INSTANCE);
    IElementType INVALID_NEXT_LINE = new IElementType("INVALID_NEXT_LINE", LuaLanguage.INSTANCE);
    IElementType BLOCK_START = new IElementType("BLOCK_START", LuaLanguage.INSTANCE);
    IElementType BLOCK_END = new IElementType("BLOCK_END", LuaLanguage.INSTANCE);
}
