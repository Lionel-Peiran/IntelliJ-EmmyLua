

package com.tang.intellij.lua.psi;

import com.intellij.psi.tree.IElementType;
import com.tang.intellij.lua.lang.LuaLanguage;

public interface LuaRegionTypes {
    IElementType REGION_START = new IElementType("REGION_START", LuaLanguage.INSTANCE);
    IElementType REGION_DESC = new IElementType("REGION_DESC", LuaLanguage.INSTANCE);
    IElementType REGION_END = new IElementType("REGION_END", LuaLanguage.INSTANCE);
}
