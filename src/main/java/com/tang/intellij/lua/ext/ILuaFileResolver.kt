

package com.tang.intellij.lua.ext

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface ILuaFileResolver {
    companion object {
        private val EP_NAME = ExtensionPointName.create<ILuaFileResolver>("com.tang.intellij.lua.luaFileResolver")

        fun findLuaFile(project: Project, shortUrl: String, extNames: Array<String>): VirtualFile? {
            for (resolver in EP_NAME.extensions) {
                val file = resolver.find(project, shortUrl, extNames)
                if (file != null)
                    return file
            }
            return null
        }
    }

    fun find(project: Project, shortUrl: String, extNames: Array<String>): VirtualFile?
}