

package com.tang.intellij.lua.reference

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.tang.intellij.lua.lang.type.LuaString
import com.tang.intellij.lua.psi.LuaCallExpr
import com.tang.intellij.lua.psi.LuaExprStat
import com.tang.intellij.lua.psi.LuaElementFactory
import com.tang.intellij.lua.psi.resolveRequireFile

/**
 *
 * Created by tangzx on 2016/12/9.
 */
class LuaRequireReference internal constructor(callExpr: LuaCallExpr) : PsiReferenceBase<LuaCallExpr>(callExpr) {

    private var pathString: String? = null
    private var range = TextRange.EMPTY_RANGE
    private val path: PsiElement? = callExpr.firstStringArg
    private var quot: String = "\""

    init {
        if (path != null && path.textLength > 2) {
            val text = path.text
            val luaString = LuaString.getContent(text)
            pathString = luaString.value
            quot = text.substring(0, luaString.start)

            if (pathString != null) {
                val start = path.textOffset - callExpr.textOffset + luaString.start
                val end = start + pathString!!.length
                range = TextRange(start, end)
            }
        }
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return myElement.manager.areElementsEquivalent(element, resolve())
    }

    override fun getRangeInElement(): TextRange {
        return range
    }

    override fun resolve(): PsiElement? {
        return if (pathString == null) null else resolveRequireFile(pathString, myElement.project)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        pathString?.let {
            val name = FileUtil.getNameWithoutExtension(newElementName)
            val last = it.lastIndexOf('.')
            val path = if (last == -1) name else it.substring(0, last) + "." + name
            setPath(path)
        }
        return myElement
    }

    fun setPath(luaPath: String) {
        if (path != null) {
            val stat = LuaElementFactory.createWith(myElement.project, "require $quot$luaPath$quot") as LuaExprStat
            val stringArg = (stat.expr as? LuaCallExpr)?.firstStringArg
            if (stringArg != null)
                path.replace(stringArg)
        }
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        return null
    }

    override fun getVariants(): Array<Any> = emptyArray()
}
