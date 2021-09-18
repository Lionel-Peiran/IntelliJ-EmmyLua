

package com.tang.intellij.lua.psi

import com.intellij.ui.RowIcon
import com.intellij.util.BitUtil
import com.tang.intellij.lua.lang.LuaIcons
import javax.swing.Icon

enum class Visibility(val text: String, val icon: Icon, val bitMask: Int) {
    PUBLIC("public", LuaIcons.PUBLIC, 0x1),
    PRIVATE("private", LuaIcons.PRIVATE, 0x2),
    PROTECTED("protected", LuaIcons.PROTECTED, 0x4);

    override fun toString() = text

    fun warpIcon(oriIcon: Icon): Icon {
        return RowIcon(oriIcon, icon)
    }

    companion object {
        fun get(text: String): Visibility = when (text) {
            "private" -> PRIVATE
            "protected" -> PROTECTED
            else -> PUBLIC
        }
        fun get(value: Int): Visibility = when (value) {
            PRIVATE.ordinal -> PRIVATE
            PROTECTED.ordinal -> PROTECTED
            else -> PUBLIC
        }
        fun getWithMask(flags: Int) = when {
            BitUtil.isSet(flags, PRIVATE.bitMask) -> PRIVATE
            BitUtil.isSet(flags, PROTECTED.bitMask) -> PROTECTED
            else -> PUBLIC
        }
    }
}