

package com.tang.intellij.lua.reference
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveResult

class LuaResolveResult(private val res: PsiElement) : ResolveResult{

    override fun getElement(): PsiElement? {
        return this.res;
    }

    override fun isValidResult(): Boolean {
        return true;
    }
}