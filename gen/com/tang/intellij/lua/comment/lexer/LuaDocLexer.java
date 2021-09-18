

package com.tang.intellij.lua.comment.lexer;

import com.intellij.lexer.FlexAdapter;

/**
 * Created by Client on 2018/3/2.
 */
public class LuaDocLexer extends FlexAdapter {
    public LuaDocLexer() {
        super(new _LuaDocLexer());
    }
}
