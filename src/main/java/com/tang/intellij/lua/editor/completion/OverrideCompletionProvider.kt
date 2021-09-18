

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Processor
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.tang.intellij.lua.ty.ITyClass
import com.tang.intellij.lua.ty.TyClass
import com.tang.intellij.lua.ty.TyLazyClass

/**
 * override supper
 * First Created on 2016/12/25.
 */
class OverrideCompletionProvider : LuaCompletionProvider() {
    override fun addCompletions(session: CompletionSession) {
        val completionParameters = session.parameters
        val completionResultSet = session.resultSet
        val id = completionParameters.position
        val methodDef = PsiTreeUtil.getParentOfType(id, LuaClassMethodDef::class.java)
        if (methodDef != null) {
            val context = SearchContext.get(methodDef.project)
            val classType = methodDef.guessClassType(context)
            if (classType != null) {
                val memberNameSet = mutableSetOf<String>()
                classType.processMembers(context, { _, m ->
                    m.name?.let { memberNameSet.add(it) }
                }, false)
                TyClass.processSuperClass(classType, context) { sup ->
                    addOverrideMethod(completionParameters, completionResultSet, memberNameSet, sup)
                    true
                }
            }
        }
    }

    private fun addOverrideMethod(completionParameters: CompletionParameters, completionResultSet: CompletionResultSet, memberNameSet:MutableSet<String>, sup: ITyClass) {
        val project = completionParameters.originalFile.project
        val context = SearchContext.get(project)
        val clazzName = sup.className
        LuaClassMemberIndex.processAll(TyLazyClass(clazzName), context, Processor { def ->
            if (def is LuaClassMethod) {
                def.name?.let {
                    if (memberNameSet.add(it)) {
                        val elementBuilder = LookupElementBuilder.create(def.name!!)
                                .withIcon(LuaIcons.CLASS_METHOD_OVERRIDING)
                                .withInsertHandler(OverrideInsertHandler(def))
                                .withTailText(def.paramSignature)

                        completionResultSet.addElement(elementBuilder)
                    }
                }
            }
            true
        })
    }

    internal class OverrideInsertHandler(funcBodyOwner: LuaFuncBodyOwner) : FuncInsertHandler(funcBodyOwner) {

        override val autoInsertParameters = true

        override fun createTemplate(manager: TemplateManager, paramNameDefList: Array<LuaParamInfo>): Template {
            val template = super.createTemplate(manager, paramNameDefList)
            template.addEndVariable()
            template.addTextSegment("\nend")
            return template
        }
    }
}
