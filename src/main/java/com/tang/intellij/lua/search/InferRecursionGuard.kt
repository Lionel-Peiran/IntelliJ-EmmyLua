

package com.tang.intellij.lua.search

import com.intellij.psi.PsiElement
import com.tang.intellij.lua.psi.LuaNameExpr

enum class GuardType {
    Unknown,
    GlobalName,
    RecursionCall
}

open class InferRecursionGuard(val psi: PsiElement) {
    open fun check(psi: PsiElement, type: GuardType): Boolean {
        return this.psi == psi
    }
}

fun createGuard(psi: PsiElement, type: GuardType): InferRecursionGuard? {
    if (type == GuardType.GlobalName && psi is LuaNameExpr) {
        return GlobalSearchGuard(psi)
    }
    return InferRecursionGuard(psi)
}

class GlobalSearchGuard(psi: LuaNameExpr) : InferRecursionGuard(psi) {
    private val name = psi.name

    override fun check(psi: PsiElement, type: GuardType): Boolean {
        if (type == GuardType.GlobalName && psi is LuaNameExpr && psi.name == name) {
            //println("guard global name: $name")
            return true
        }
        return super.check(psi, type)
    }
}