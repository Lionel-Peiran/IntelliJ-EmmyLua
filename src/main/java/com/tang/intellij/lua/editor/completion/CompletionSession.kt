

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.util.Key
import java.util.*

/**
 *
 * First Created on 2017/5/22.
 */
class CompletionSession(val parameters: CompletionParameters, val resultSet: CompletionResultSet) {
    var isSuggestWords = true

    private val words = HashSet<String>()

    fun addWord(word: String): Boolean {
        return words.add(word)
    }

    companion object {

        val KEY = Key.create<CompletionSession>("lua.CompletionSession")

        operator fun get(completionParameters: CompletionParameters): CompletionSession {
            return completionParameters.editor.getUserData(KEY)!!
        }
    }
}
