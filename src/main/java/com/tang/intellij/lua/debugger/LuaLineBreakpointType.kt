

package com.tang.intellij.lua.debugger

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase
import com.tang.intellij.lua.lang.LuaFileType

/**
 *
 * First Created on 2016/12/30.
 */
class LuaLineBreakpointType : XLineBreakpointTypeBase(ID, NAME, LuaDebuggerEditorsProvider()) {

    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        return file.fileType === LuaFileType.INSTANCE
    }

    companion object {

        private const val ID = "lua-line"
        private const val NAME = "lua-line-breakpoint"
    }
}
