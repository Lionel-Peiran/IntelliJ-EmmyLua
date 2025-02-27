

package com.tang.intellij.lua.codeInsight.highlighting

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Consumer
import com.tang.intellij.lua.comment.psi.LuaDocTagOverload
import com.tang.intellij.lua.comment.psi.LuaDocTagReturn
import com.tang.intellij.lua.psi.*

/**

 * Created by tangzx on 2017/3/18.
 */
class LuaHighlightExitPointsHandler internal constructor(editor: Editor, file: PsiFile, private val target: LuaReturnStat, private val funcBody: LuaFuncBody) : HighlightUsagesHandlerBase<PsiElement>(editor, file) {

    override fun getTargets(): List<PsiElement> {
        return listOf<PsiElement>(target)
    }

    override fun selectTargets(list: List<PsiElement>, consumer: Consumer<in List<PsiElement>>) {
    }

    override fun computeUsages(list: List<PsiElement>) {
        funcBody.acceptChildren(object : LuaVisitor() {
            override fun visitReturnStat(o: LuaReturnStat) {
                addOccurrence(o)
            }

            override fun visitFuncBodyOwner(o: LuaFuncBodyOwner) {
                // ignore sub function
            }

            override fun visitClosureExpr(o: LuaClosureExpr) {

            }

            override fun visitPsiElement(o: LuaPsiElement) {
                o.acceptChildren(this)
            }
        })

        val parent = funcBody.parent
        if (parent is LuaCommentOwner) {
            val comment = parent.comment
            val returnDef = PsiTreeUtil.findChildOfType(comment, LuaDocTagReturn::class.java)
            if (returnDef != null)
                addOccurrence(returnDef)

            val overloads = PsiTreeUtil.findChildrenOfType(comment, LuaDocTagOverload::class.java)
            overloads.forEach { overload ->
                overload.functionTy?.typeList?.let { addOccurrence(it) }
            }
        }
    }
}
