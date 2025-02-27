

package com.tang.intellij.lua.codeInsight.editorActions;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.tree.TokenSet;
import com.tang.intellij.lua.psi.LuaTypes;

/**
 * LuaQuoteHandler
 * Created by tangzx on 2017/2/19.
 */
public class LuaQuoteHandler extends SimpleTokenSetQuoteHandler {
    public LuaQuoteHandler() {
        super(TokenSet.create(LuaTypes.STRING));
    }
}
