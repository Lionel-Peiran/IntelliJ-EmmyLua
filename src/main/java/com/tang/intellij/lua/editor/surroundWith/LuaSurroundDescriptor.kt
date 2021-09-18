

package com.tang.intellij.lua.editor.surroundWith

import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.lang.surroundWith.Surrounder
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.psi.LuaStatement
import java.util.*

/**
 * SurroundDescriptor
 * Created by tangzx on 2017/2/25.
 */
class LuaSurroundDescriptor : SurroundDescriptor {
    private val surrounders = arrayOf<Surrounder>(
            RegionSurrounder("Lua Region --region", "region", "endregion"),
            RegionSurrounder("Lua Region --{{{", "{{{", "}}}")
    )

    override fun getElementsToSurround(psiFile: PsiFile, startOffset: Int, endOffset: Int): Array<PsiElement> {
        return findStatementsInRange(psiFile, startOffset, endOffset)
    }

    private fun findStatementsInRange(file: PsiFile, start: Int, end: Int): Array<PsiElement> {
        var startOffset = start
        var endOffset = end
        var element1 = file.viewProvider.findElementAt(startOffset, LuaLanguage.INSTANCE)
        var element2 = file.viewProvider.findElementAt(endOffset - 1, LuaLanguage.INSTANCE)
        if (element1 is PsiWhiteSpace) {
            startOffset = element1.textRange.endOffset
            element1 = file.findElementAt(startOffset)
        }
        if (element2 is PsiWhiteSpace) {
            endOffset = element2.textRange.startOffset
            element2 = file.findElementAt(endOffset - 1)
        }
        if (element1 == null || element2 == null)
            return PsiElement.EMPTY_ARRAY

        var parent = PsiTreeUtil.findCommonParent(element1, element2) ?: return PsiElement.EMPTY_ARRAY
        while (true) {
            if (parent is LuaStatement) {
                if (element1 !is PsiComment) {
                    parent = parent.parent
                }
                break
            }
            if (parent is PsiFile) break
            parent = parent.parent
        }

        if (parent != element1) {
            while (parent != element1!!.parent) {
                element1 = element1.parent
            }
        }

        if (parent != element2) {
            while (parent != element2!!.parent) {
                element2 = element2.parent
            }
        }

        val children = parent.children
        val array = ArrayList<PsiElement>()
        var flag = false
        for (child in children) {
            if (child == element1) {
                flag = true
            }
            if (flag && child !is PsiWhiteSpace) {
                array.add(child)
            }
            if (child == element2) {
                break
            }
        }

        for (element in array) {
            if (!(element is LuaStatement || element is PsiWhiteSpace || element is PsiComment)) {
                return PsiElement.EMPTY_ARRAY
            }
        }

        return PsiUtilCore.toPsiElementArray(array)
    }

    override fun getSurrounders(): Array<Surrounder> {
        return surrounders
    }

    override fun isExclusive(): Boolean {
        return false
    }
}