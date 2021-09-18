

package com.tang.intellij.lua.codeInsight.intention

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.Nls

/**
 * 创建方法
 * First Created on 2017/4/13.
 */
class CreateMethodIntention : BaseIntentionAction() {
    @Nls
    override fun getFamilyName(): String {
        return "Create method"
    }

    override fun getText(): String {
        return familyName
    }

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        /*val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false)
        if (callExpr != null && !callExpr.isFunctionCall) {
            val bodyOwner = callExpr.resolveFuncBodyOwner(SearchContext.get(project))
            return bodyOwner == null
        }*/
        return false
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        /*val callExpr = LuaPsiTreeUtil.findElementOfClassAtOffset(psiFile, editor.caretModel.offset, LuaCallExpr::class.java, false)
        if (callExpr != null && !callExpr.isFunctionCall) {
            val expr = callExpr.expr
            if (expr is LuaIndexExpr) {
                val type = expr.guessParentType(SearchContext.get(project))
                if (Ty.isInvalid(type)) return

                val position = calcInsertPosition(TyUnion.getPerfectClass(type), project)
                if (position != null) {
                    editor.caretModel.moveToOffset(position.offset)

                    val manager = TemplateManager.getInstance(project)
                    val template = manager.createTemplate("", "", String.format("\n\nfunction %s:\$NAME$()\n\$END$\nend", position.prefix))

                    template.addVariable("NAME", null, TextExpression(expr.name), true)
                    manager.startTemplate(editor, template)
                }
            }
        }*/
    }

    /*private class InsertPosition {
        internal var offset: Int = 0
        internal var prefix: String? = null
    }

    private fun calcInsertPosition(perfect: ITyClass?, project: Project): InsertPosition? {
        val methods = LuaClassMethodIndex.instance.get(perfect!!.className,
                project,
                LuaPredefinedScope(project))
        if (!methods.isEmpty()) {
            val methodDef = methods.iterator().next()
            if (methodDef is LuaClassMethodDef) {
                val expr = methodDef.classMethodName.expr
                val textRange = methodDef.getTextRange()
                val position = InsertPosition()
                position.offset = textRange.endOffset
                position.prefix = expr.text
                return position
            }
        }
        return null
    }*/
}
