

package com.tang.intellij.lua.lang.type

import java.util.regex.Pattern

/**
 *
 * Created by tangzx on 2016/12/25.
 */
class LuaString {
    var start: Int = 0
    var end: Int = 0
    var value = ""

    val length: Int
        get() = end - start

    companion object {

        /**
         * 获取 lua 字符串的内容，
         * "value"
         * 'value'
         * [[value]]
         * @param text string element
         * @return value of String
         */
        fun getContent(text: String): LuaString {
            val content = LuaString()
            if (text.startsWith("[")) {
                val pattern = Pattern.compile("\\[(=*)\\[([\\s\\S]*)]\\1]")
                val matcher = pattern.matcher(text)
                if (matcher.find()) {
                    val contentString = matcher.group(2)
                    content.start = matcher.start(2)
                    content.end = matcher.end(2)
                    content.value = contentString
                }
            } else {
                content.start = 1
                content.end = text.length - 1
                if (content.end > content.start)
                    content.value = text.substring(content.start, content.end)
                else
                    content.end = content.start
            }
            return content
        }
    }
}
