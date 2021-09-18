

package com.tang.intellij.lua.debugger

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase
import com.tang.intellij.lua.lang.LuaFileType
import com.tang.intellij.lua.psi.impl.LuaExprCodeFragmentImpl

/**
 *
 * First Created on 2016/12/30.
 */
class LuaDebuggerEditorsProvider : XDebuggerEditorsProviderBase() {
    override fun createExpressionCodeFragment(project: Project, text: String, context: PsiElement?, isPhysical: Boolean): PsiFile {
        val fragment = LuaExprCodeFragmentImpl(project,"fragment.lua", text, isPhysical)
        fragment.context = context
        return fragment
    }

    override fun getFileType(): FileType {
        return LuaFileType.INSTANCE
    }
}