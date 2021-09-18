

package com.tang.intellij.lua.ext

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

class LuaFileAbsoluteResolver : ILuaFileResolver {
    override fun find(project: Project, shortUrl: String, extNames: Array<String>): VirtualFile? {
        //绝对路径
        for (ext in extNames) {
            val file = VirtualFileManager.getInstance().findFileByUrl(VfsUtil.pathToUrl(shortUrl + ext))
            if (file != null && !file.isDirectory) {
                return file
            }
        }
        return null
    }
}