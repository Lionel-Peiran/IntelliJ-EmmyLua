

package com.tang.intellij.lua.highlighting

import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import com.tang.intellij.lua.lang.LuaFileType

/**
 *
 * Created by tangzx on 2017/1/11.
 */
class LuaProblemFileHighlightFilter : Condition<VirtualFile> {
    override fun value(file: VirtualFile): Boolean {
        return file.fileType === LuaFileType.INSTANCE
    }
}
