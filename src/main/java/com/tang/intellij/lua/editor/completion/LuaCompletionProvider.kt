

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

abstract class LuaCompletionProvider : CompletionProvider<CompletionParameters>() {

    protected var session: CompletionSession? = null

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
        val session = CompletionSession[parameters]
        this.session = session
        addCompletions(session)
    }

    abstract fun addCompletions(session: CompletionSession)
}