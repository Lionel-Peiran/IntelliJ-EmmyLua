

package com.tang.intellij.lua.lexer

import com.tang.intellij.lua.lang.LuaLanguageLevel
import com.tang.intellij.lua.project.LuaSettings

/**
 * Created by tangzx on 2015/11/15.
 * Email:love.tangzx@qq.com
 */
class LuaLexerAdapter(level: LuaLanguageLevel = LuaSettings.instance.languageLevel) : LuaLexer(level)
