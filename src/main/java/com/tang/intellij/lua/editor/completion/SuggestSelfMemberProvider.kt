

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.psi.LuaClassMember
import com.tang.intellij.lua.psi.LuaClassMethodDef
import com.tang.intellij.lua.psi.LuaPsiTreeUtil
import com.tang.intellij.lua.psi.guessClassType
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.ITyFunction
import com.tang.intellij.lua.ty.isVisibleInScope

/**
 * suggest self.xxx
 * First Created on 2017/4/11.
 */
class SuggestSelfMemberProvider : ClassMemberCompletionProvider() {
    override fun addCompletions(session: CompletionSession) {
        val completionParameters = session.parameters
        val completionResultSet = session.resultSet
        val position = completionParameters.position
        val methodDef = PsiTreeUtil.getParentOfType(position, LuaClassMethodDef::class.java)
        if (methodDef != null && !methodDef.isStatic) {
            val project = position.project
            val searchContext = SearchContext.get(project)
            methodDef.guessClassType(searchContext)?.let { type ->
                val contextTy = LuaPsiTreeUtil.findContextClass(position)
                type.processMembers(searchContext) { curType, member ->
                    if (curType.isVisibleInScope(project, contextTy, member.visibility)) {
                        addMember(completionResultSet,
                                member,
                                curType,
                                type,
                                MemberCompletionMode.Colon,
                                project,
                                object : HandlerProcessor() {
                                    override fun process(element: LuaLookupElement, member: LuaClassMember, memberTy: ITy?): LookupElement { return element }

                                    override fun processLookupString(lookupString: String, member: LuaClassMember, memberTy: ITy?): String {
                                        return if (memberTy is ITyFunction) "self:${member.name}" else "self.${member.name}"
                                    }
                                })
                    }
                }
            }
        }
    }
}