

package com.tang.intellij.lua.ext

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.tang.intellij.lua.project.LuaSourceRootManager

class LuaFileSourcesRootResolver : ILuaFileResolver {
    override fun find(project: Project, shortUrl: String, extNames: Array<String>): VirtualFile? {
        for (sourceRoot in LuaSourceRootManager.getInstance(project).getSourceRootUrls()) {
            val file = findFile(shortUrl, sourceRoot, extNames)
            if (file != null) return file
        }
        return null
    }

    private fun findFile(shortUrl: String, root: String, extensions: Array<String>): VirtualFile? {
        for (ext in extensions) {
            var fixedURL = shortUrl
            if (shortUrl.endsWith(ext)) { //aa.bb.lua -> aa.bb
                fixedURL = shortUrl.substring(0, shortUrl.length - ext.length)
            }

            //将.转为/，但不处理 ..
            if (!fixedURL.contains("/")) {
                //aa.bb -> aa/bb
                fixedURL = fixedURL.replace("\\.".toRegex(), "/")
            }

            fixedURL += ext

            val file = VirtualFileManager.getInstance().findFileByUrl("$root/$fixedURL")
            if (file != null && !file.isDirectory) {
                return file
            }
        }
        return null
    }
}