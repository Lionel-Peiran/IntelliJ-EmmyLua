

package com.tang.intellij.lua.psi

import com.intellij.psi.PsiElement

/**
 * for, repeat, while
 * Created by Administrator on 2017/6/28.
 */
interface LuaLoop : LuaPsiElement


val LuaLoop.head:PsiElement? get() {
    val type = when (this) {
        is LuaWhileStat -> LuaTypes.WHILE
        is LuaRepeatStat -> LuaTypes.REPEAT
        else -> LuaTypes.FOR
    }
    val headNode = node.findChildByType(type)
    return headNode?.psi
}
val LuaLoop.end:PsiElement? get() {
    val type = when (this) {
        is LuaRepeatStat -> LuaTypes.UNTIL
        else -> LuaTypes.END
    }
    val endNode = node.findChildByType(type)
    return endNode?.psi
}
