

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.codeStyle.NameUtil
import com.intellij.util.Processor
import com.tang.intellij.lua.editor.LuaNameSuggestionProvider
import com.tang.intellij.lua.psi.search.LuaShortNamesManager

class SuggestLocalNameProvider : LuaCompletionProvider() {
    override fun addCompletions(session: CompletionSession) {
        val project = session.parameters.position.project
        LuaShortNamesManager.getInstance(project).processAllClassNames(project, Processor{ className ->
            NameUtil.getSuggestionsByName(className, "", "", false, false, false).forEach {
                val name = LuaNameSuggestionProvider.fixName(it)
                if (session.addWord(name)) {
                    session.resultSet.addElement(LookupElementBuilder.create(name).withIcon(AllIcons.Actions.RefactoringBulb))
                }
            }
            true
        })
    }
}