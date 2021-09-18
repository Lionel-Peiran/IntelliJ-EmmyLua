

package com.tang.intellij.lua.lang

enum class LuaLanguageLevel(val version: Int) {
    LUA50(50),
    LUA51(51),
    LUA52(52),
    LUA53(53),
    LUA54(54);

    override fun toString(): String {
        return "Lua ${version / 10}.${version % 10}"
    }
}