

package com.tang.intellij.lua.psi;

import com.intellij.psi.tree.IElementType;
import com.tang.intellij.lua.lang.LuaLanguage;

/**
 * First Created on 2015/11/15.
 * Email:love.tangzx@qq.com
 */
public class LuaTokenType extends IElementType {
    public LuaTokenType(String debugName) {
        super(debugName, LuaLanguage.INSTANCE);
    }
}
