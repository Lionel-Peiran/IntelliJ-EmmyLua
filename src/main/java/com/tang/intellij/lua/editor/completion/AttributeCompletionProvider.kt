

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.lookup.LookupElementBuilder

class AttributeCompletionProvider : LuaCompletionProvider() {

    private val names = arrayOf("const", "close")

    override fun addCompletions(session: CompletionSession) {
         names.forEach { name ->
             session.addWord(name)
             session.resultSet.addElement(LookupElementBuilder.create(name))
         }
        session.resultSet.stopHere()
    }
}