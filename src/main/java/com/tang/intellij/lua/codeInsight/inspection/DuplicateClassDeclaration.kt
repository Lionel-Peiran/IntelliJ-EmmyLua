

package com.tang.intellij.lua.codeInsight.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.tang.intellij.lua.LuaBundle
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.comment.psi.LuaDocVisitor
import com.tang.intellij.lua.psi.search.LuaShortNamesManager
import com.tang.intellij.lua.search.SearchContext

/**
 * 重复定义class
 * Created by TangZX on 2016/12/16.
 */
class DuplicateClassDeclaration : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor {
        return object : LuaDocVisitor() {
            override fun visitTagClass(o: LuaDocTagClass) {
                val useScope = o.useScope as? GlobalSearchScope ?: return
                val identifier = o.nameIdentifier
                val project = o.project
                val context = SearchContext.get(project)
                context.withScope(useScope) {
                    LuaShortNamesManager
                            .getInstance(project)
                            .processClassesWithName(identifier.text, context, Processor {
                        val path = it.containingFile?.virtualFile?.canonicalPath
                        if (it != o && path != null) {
                            holder.registerProblem(
                                    identifier,
                                    LuaBundle.message("inspection.duplicate_class", path),
                                    ProblemHighlightType.GENERIC_ERROR)
                        }
                        true
                    })
                }
            }
        }
    }
}
