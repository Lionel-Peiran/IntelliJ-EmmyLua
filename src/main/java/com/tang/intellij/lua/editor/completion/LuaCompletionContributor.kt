

package com.tang.intellij.lua.editor.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import com.tang.intellij.lua.comment.LuaCommentUtil
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.project.LuaSettings
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.refactoring.LuaRefactoringUtil

/**

 * First Created on 2016/11/27.
 */
class LuaCompletionContributor : CompletionContributor() {
    private var suggestWords = true
    init {
        //可以override
        extend(CompletionType.BASIC, SHOW_OVERRIDE, OverrideCompletionProvider())

        extend(CompletionType.BASIC, IN_CLASS_METHOD, SuggestSelfMemberProvider())

        //提示属性, 提示方法
        extend(CompletionType.BASIC, SHOW_CLASS_FIELD, ClassMemberCompletionProvider())

        extend(CompletionType.BASIC, SHOW_REQUIRE_PATH, RequirePathCompletionProvider())

        extend(CompletionType.BASIC, LuaStringArgHistoryProvider.STRING_ARG, LuaStringArgHistoryProvider())

        //提示全局函数,local变量,local函数
        extend(CompletionType.BASIC, IN_NAME_EXPR, LocalAndGlobalCompletionProvider(LocalAndGlobalCompletionProvider.ALL))

        extend(CompletionType.BASIC, IN_CLASS_METHOD_NAME, LocalAndGlobalCompletionProvider(LocalAndGlobalCompletionProvider.VARS))

        extend(CompletionType.BASIC, GOTO, object : CompletionProvider<CompletionParameters>(){
            override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
                LuaPsiTreeUtil.walkUpLabel(parameters.position) {
                    val name = it.name
                    if (name != null) {
                        resultSet.addElement(LookupElementBuilder.create(name).withIcon(AllIcons.Actions.Rollback))
                    }
                    return@walkUpLabel true
                }
                resultSet.stopHere()
            }
        })

        extend(CompletionType.BASIC, psiElement(LuaTypes.ID).withParent(LuaNameDef::class.java), SuggestLocalNameProvider())

        extend(CompletionType.BASIC, IN_TABLE_FIELD, TableCompletionProvider())

        extend(CompletionType.BASIC, ATTRIBUTE, AttributeCompletionProvider())
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val session = CompletionSession(parameters, result)
        parameters.editor.putUserData(CompletionSession.KEY, session)
        super.fillCompletionVariants(parameters, result)
        if (LuaSettings.instance.isShowWordsInFile && suggestWords && session.isSuggestWords && !result.isStopped) {
            suggestWordsInFile(parameters)
        }
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        suggestWords = true
        val file = context.file
        if (file is LuaPsiFile) {
            val element = file.findElementAt(context.caret.offset - 1)
            if (element != null) {
                if (element.parent is LuaLabelStat) {
                    suggestWords = false
                    context.dummyIdentifier = ""
                } else if (!LuaCommentUtil.isComment(element)) {
                    val type = element.node.elementType
                    if (type in IGNORE_SET) {
                        suggestWords = false
                        context.dummyIdentifier = ""
                    }
                }
            }
        }
    }

    companion object {
        private val IGNORE_SET = TokenSet.create(LuaTypes.STRING, LuaTypes.NUMBER, LuaTypes.CONCAT)

        private val SHOW_CLASS_FIELD = psiElement(LuaTypes.ID)
                .withParent(LuaIndexExpr::class.java)

        private val IN_FUNC_NAME = psiElement(LuaTypes.ID)
                .withParent(LuaIndexExpr::class.java)
                .inside(LuaClassMethodName::class.java)
        private val AFTER_FUNCTION = psiElement()
                .afterLeaf(psiElement(LuaTypes.FUNCTION))
        private val IN_CLASS_METHOD_NAME = psiElement().andOr(IN_FUNC_NAME, AFTER_FUNCTION)

        private val IN_NAME_EXPR = psiElement(LuaTypes.ID)
                .withParent(LuaNameExpr::class.java)

        private val SHOW_OVERRIDE = psiElement()
                .withParent(LuaClassMethodName::class.java)
        private val IN_CLASS_METHOD = psiElement(LuaTypes.ID)
                .withParent(LuaNameExpr::class.java)
                .inside(LuaClassMethodDef::class.java)
        private val SHOW_REQUIRE_PATH = psiElement(LuaTypes.STRING)
                .withParent(
                        psiElement(LuaTypes.LITERAL_EXPR).withParent(
                                psiElement(LuaArgs::class.java).afterSibling(
                                        psiElement().with(RequireLikePatternCondition())
                                )
                        )
                )

        private val GOTO = psiElement(LuaTypes.ID).withParent(LuaGotoStat::class.java)

        private val IN_TABLE_FIELD = psiElement().andOr(
                psiElement().withParent(
                        psiElement(LuaTypes.NAME_EXPR).withParent(LuaTableField::class.java)
                ),
                psiElement(LuaTypes.ID).withParent(LuaTableField::class.java)
        )

        private val ATTRIBUTE = psiElement(LuaTypes.ID).withParent(LuaAttribute::class.java)

        private fun suggestWordsInFile(parameters: CompletionParameters) {
            val session = CompletionSession[parameters]
            val originalPosition = parameters.originalPosition
            if (originalPosition != null)
                session.addWord(originalPosition.text)
            
            val wordsScanner = LanguageFindUsages.INSTANCE.forLanguage(LuaLanguage.INSTANCE).wordsScanner
            wordsScanner?.processWords(parameters.editor.document.charsSequence) {
                val word = it.baseText.subSequence(it.start, it.end).toString()
                if (word.length > 2 && LuaRefactoringUtil.isLuaIdentifier(word) && session.addWord(word)) {
                    session.resultSet.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder
                            .create(word)
                            .withIcon(LuaIcons.WORD), -1.0)
                    )
                }
                true
            }
        }
    }
}

class RequireLikePatternCondition : PatternCondition<PsiElement>("requireLike"){
    override fun accepts(psi: PsiElement, context: ProcessingContext?): Boolean {
        val name = (psi as? PsiNamedElement)?.name
        return if (name != null) LuaSettings.isRequireLikeFunctionName(name) else false
    }
}