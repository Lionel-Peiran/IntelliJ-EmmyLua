

package com.tang.intellij.lua.comment.psi;

import com.intellij.psi.tree.IElementType;
import com.tang.intellij.lua.lang.LuaLanguage;

public class LuaDocTokenType extends IElementType {
    public LuaDocTokenType(String debugName) {
        super(debugName, LuaLanguage.INSTANCE);
    }
}
