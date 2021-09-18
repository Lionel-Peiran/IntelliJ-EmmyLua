

package com.tang.intellij.lua.project

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.PathUtil
import com.intellij.util.indexing.IndexableSetContributor
import com.intellij.util.io.URLUtil
import com.tang.intellij.lua.psi.LuaFileUtil
import java.io.File

/**
 *
 * Created by Administrator on 2017/7/5.
 */
class LuaPredefinedLibraryProvider : IndexableSetContributor() {

    private val predefined: Set<VirtualFile> by lazy {
        val jarPath = PathUtil.getJarPathForClass(LuaPredefinedLibraryProvider::class.java)
        val dir = if (jarPath.endsWith(".jar")) {
            VfsUtil.findFileByURL(URLUtil.getJarEntryURL(File(jarPath), "std"))
        } else
            VfsUtil.findFileByIoFile(File("$jarPath/std"), true)

        val set = mutableSetOf<VirtualFile>()
        if (dir != null) {
            dir.children.forEach {
                it.putUserData(LuaFileUtil.PREDEFINED_KEY, true)
            }
            set.add(dir)
        }
        set
    }

    override fun getAdditionalRootsToIndex(): Set<VirtualFile> {
        return predefined
    }
}