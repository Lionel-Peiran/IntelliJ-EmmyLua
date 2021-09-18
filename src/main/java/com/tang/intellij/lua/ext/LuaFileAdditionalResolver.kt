

package com.tang.intellij.lua.ext

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.tang.intellij.lua.project.LuaSettings

class LuaFileAdditionalResolver : ILuaFileResolver {
    override fun find(project: Project, shortUrl: String, extNames: Array<String>): VirtualFile? {
        val sourcesRoot = LuaSettings.instance.additionalSourcesRoot
        for (sr in sourcesRoot) {
            for (ext in extNames) {
                val path = "$sr/$shortUrl$ext"
                val file = VirtualFileManager.getInstance().findFileByUrl(VfsUtil.pathToUrl(path))
                if (file != null && !file.isDirectory) {
                    return file
                }
            }
        }
        return null
    }
}