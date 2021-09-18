

package com.tang.intellij.lua.usages

import com.intellij.psi.PsiElement
import com.intellij.usages.UsageTarget
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProviderEx
import com.tang.intellij.lua.psi.LuaCallExpr
import com.tang.intellij.lua.psi.LuaPsiElement

class UsageTypeProvider : UsageTypeProviderEx {
    companion object {
        val FUNCTION_CALL = UsageType("Function call")
    }

    override fun getUsageType(element: PsiElement?, targets: Array<out UsageTarget>): UsageType? {
        if (element is LuaPsiElement) {
            val parent = element.parent
            if (parent is LuaCallExpr)
                return FUNCTION_CALL
        }
        return null
    }

    override fun getUsageType(element: PsiElement): UsageType? {
        return getUsageType(element, emptyArray())
    }
}