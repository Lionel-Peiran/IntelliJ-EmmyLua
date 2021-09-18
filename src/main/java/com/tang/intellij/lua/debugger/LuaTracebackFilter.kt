

package com.tang.intellij.lua.debugger

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.OpenFileHyperlinkInfo
import com.intellij.openapi.project.Project
import com.tang.intellij.lua.psi.LuaFileUtil
import java.util.regex.Pattern

/**
 *
 * First Created on 2017/6/10.
 */
class LuaTracebackFilter(private val project: Project) : Filter {

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        //lua.exe: Test.lua:3: attempt to call global 'print1' (a nil value)
        //stack traceback:
        //Test.lua:3: in function 'a'
        //Test.lua:7: in function 'b'
        //Test.lua:11: in main chunk

        val pattern = Pattern.compile("\\s*((/+)?[^<>\\\\|:\"*? ]+):(\\d+):")
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            val fileName = matcher.group(1)
            val lineNumber = Integer.parseInt(matcher.group(3))
            val file = LuaFileUtil.findFile(project, fileName)
            if (file != null) {
                val hyperlink = OpenFileHyperlinkInfo(project, file, lineNumber - 1)
                val textStartOffset = entireLength - line.length
                val startPos = matcher.start(1)
                val endPos = matcher.end(3) + 1
                return Filter.Result(startPos + textStartOffset, endPos + textStartOffset, hyperlink)
            }
        }
        return null
    }
}
