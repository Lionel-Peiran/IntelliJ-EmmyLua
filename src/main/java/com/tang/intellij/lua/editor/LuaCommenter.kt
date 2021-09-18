

package com.tang.intellij.lua.editor

import com.intellij.codeInsight.generation.IndentedCommenter
import com.intellij.lang.Commenter

/**
 * Lua Commenter
 * First Created on 2016/12/15.
 */
class LuaCommenter : Commenter, IndentedCommenter {
    override fun getLineCommentPrefix(): String? {
        return "--"
    }

    override fun getBlockCommentPrefix(): String? {
        return "--[["
    }

    override fun getBlockCommentSuffix(): String? {
        return "]]"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }

    override fun forceIndentedLineComment(): Boolean? {
        return true
    }
}
