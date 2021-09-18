

package com.tang.intellij.lua.project

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

/**
 * lua ModuleBuilder
 * Created by tangzx on 2016/12/24.
 */
class LuaModuleBuilder : ModuleBuilder() {

    override fun setupRootModel(rootModel: ModifiableRootModel) {
        rootModel.sdk = StdSDK.sdk

        val contentEntry = doAddContentEntry(rootModel)
        if (contentEntry != null) {
            val sourcePaths = getSourcePaths()

            for (sourcePath in sourcePaths) {
                val path = sourcePath.first
                File(path).mkdirs()
                val sourceRoot = LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(path))
                if (sourceRoot != null) {
                    contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second)
                }
            }
        }
    }

    override fun getModuleType() = LuaModuleType.instance

    private fun getSourcePaths(): List<Pair<String, String>> {
        val sourcePaths = mutableListOf<Pair<String, String>>()
        val path = contentEntryPath + File.separator + "src"
        File(path).mkdirs()
        sourcePaths.add(Pair.create(path, ""))
        return sourcePaths
    }
}
