

package com.tang.intellij.lua.psi

import com.intellij.psi.PsiElement

abstract class LuaRecursiveVisitor : LuaVisitor() {
    override fun visitElement(element: PsiElement) {
        element.acceptChildren(this)
    }
}

abstract class LuaStubRecursiveVisitor : LuaRecursiveVisitor() {
    override fun visitElement(element: PsiElement) {
        var stub: STUB_ELE? = null
        if (element is LuaPsiFile) {
            stub = element.stub
        }
        if (element is STUB_PSI) {
            stub  = element.stub
        }
        if (stub != null) {
            for (child in stub.childrenStubs) {
                child.psi.accept(this)
            }
        } else super.visitElement(element)
    }
}