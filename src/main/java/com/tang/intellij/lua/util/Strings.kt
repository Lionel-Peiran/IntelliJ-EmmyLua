

package com.tang.intellij.lua.util

import org.jetbrains.annotations.Contract

class Strings {
    companion object {
        @Contract(pure = true)
        fun stringHashCode(chars: CharSequence, from: Int, to: Int): Int {
            return stringHashCode(chars, from, to, 0)
        }

        @Contract(pure = true)
        fun stringHashCode(chars: CharSequence, from: Int, to: Int, prefixHash: Int): Int {
            var h = prefixHash
            for (off in from until to) {
                h = 31 * h + chars[off].code
            }
            return h
        }

        @Contract(pure = true)
        fun stringHashCode(chars: CharArray, from: Int, to: Int): Int {
            var h = 0
            for (off in from until to) {
                h = 31 * h + chars[off].code
            }
            return h
        }
    }
}

fun StringBuilder.appendLine(line: String): StringBuilder = append(line).append("\n")