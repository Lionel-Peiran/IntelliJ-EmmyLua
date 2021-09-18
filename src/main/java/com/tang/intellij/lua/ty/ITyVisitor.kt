

package com.tang.intellij.lua.ty

interface ITyVisitor {
    fun visitTy(ty: ITy)

    fun visitClass(clazz: ITyClass)

    fun visitFun(f: ITyFunction)

    fun visitUnion(u: TyUnion)

    fun visitTuple(tuple: TyTuple)

    fun visitArray(array: ITyArray)

    fun visitGeneric(generic: ITyGeneric)
}

open class TyVisitor : ITyVisitor {
    override fun visitTy(ty: ITy) {
        ty.acceptChildren(this)
    }

    override fun visitClass(clazz: ITyClass) {
        visitTy(clazz)
    }

    override fun visitFun(f: ITyFunction) {
        visitTy(f)
    }

    override fun visitUnion(u: TyUnion) {
        visitTy(u)
    }

    override fun visitTuple(tuple: TyTuple) {
        visitTy(tuple)
    }

    override fun visitArray(array: ITyArray) {
        visitTy(array)
    }

    override fun visitGeneric(generic: ITyGeneric) {
        visitTy(generic)
    }
}