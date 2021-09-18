

package com.tang.intellij.lua.lang

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.util.io.ByteSequence
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile

class LuaFileTypeDetector : FileTypeRegistry.FileTypeDetector {
    override fun getVersion() = 1

    override fun detect(file: VirtualFile, firstBytes: ByteSequence, firstCharsIfText: CharSequence?): FileType? {
        return if (FileUtil.isHashBangLine(firstCharsIfText, "lua")) LuaFileType.INSTANCE else null
    }
}