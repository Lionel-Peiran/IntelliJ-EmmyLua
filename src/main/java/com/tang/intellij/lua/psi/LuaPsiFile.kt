

package com.tang.intellij.lua.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.tang.intellij.lua.Constants
import com.tang.intellij.lua.comment.psi.api.LuaComment
import com.tang.intellij.lua.lang.LuaFileType
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.project.LuaSettings
import com.tang.intellij.lua.stubs.LuaFileStub
import com.tang.intellij.lua.util.Strings

/**
 * First Created on 2015/11/15.
 * Email:love.tangzx@qq.com
 */
open class LuaPsiFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, LuaLanguage.INSTANCE), LuaTypeGuessable, LuaDeclarationScope {

    override fun getFileType(): FileType {
        return LuaFileType.INSTANCE
    }

    val uid: String get() {
        val stub = greenStub
        if (stub is LuaFileStub)
            return stub.uid
        val file = originalFile
        val contents = file.viewProvider.contents
        val hashCode = Strings.stringHashCode(contents, 0, contents.length)
        return "$name[$hashCode]"
    }

    val tooLarger: Boolean get() {
        val fileLimit = LuaSettings.instance.tooLargerFileThreshold * 1024
        val fileSize = viewProvider.virtualFile.length
        return fileSize > fileLimit
    }

    override fun setName(name: String): PsiElement {
        return if (FileUtil.getNameWithoutExtension(name) == name) {
            super.setName("$name.${LuaFileType.INSTANCE.defaultExtension}")
        } else super.setName(name)
    }

    val moduleName: String?
        get() {
            val stub = stub as? LuaFileStub
            return if (stub != null) stub.module else findCachedModuleName()
        }

    /**
     * Lua language version
     */
    val languageLevel get() = LuaSettings.instance.languageLevel

    private fun findCachedModuleName(): String? {
        return CachedValuesManager.getCachedValue(this, KEY_CACHED_MODULE_NAME) {
            CachedValueProvider.Result.create(findModuleName(), this)
        }
    }

    private fun findModuleName():String? {
        var child: PsiElement? = firstChild
        while (child != null) {
            if (child is LuaComment) { // ---@module name
                val name = child.moduleName
                if (name != null) return name
            } else if (child is LuaStatement) {
                val comment = child.comment
                if (comment != null) {
                    val name = comment.moduleName
                    if (name != null) return name
                }
                if (child is LuaExprStat) { // module("name")
                    val callExpr = child.expr as? LuaCallExpr
                    val expr = callExpr?.expr
                    if (expr is LuaNameExpr && expr.textMatches(Constants.WORD_MODULE)) {
                        val stringArg = callExpr.firstStringArg
                        if (stringArg != null)
                            return stringArg.text
                    }
                }
            }
            child = child.nextSibling
        }
        return null
    }

    companion object {
        private val KEY_CACHED_MODULE_NAME = Key.create<CachedValue<String?>>("lua.file.module.name")
    }
}