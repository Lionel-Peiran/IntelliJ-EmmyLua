

package com.tang.intellij.lua.ty

import com.tang.intellij.lua.search.SearchContext

interface ITyPrimitive : ITy {
    val primitiveKind: TyPrimitiveKind
}

// number, boolean, nil, void ...
class TyPrimitive(override val primitiveKind: TyPrimitiveKind,
                  override val displayName: String) : Ty(TyKind.Primitive), ITyPrimitive {
    override fun equals(other: Any?): Boolean {
        return other is TyPrimitive && other.primitiveKind == primitiveKind
    }

    override fun hashCode(): Int {
        return primitiveKind.hashCode()
    }
}

// string
class TyPrimitiveClass(override val primitiveKind: TyPrimitiveKind,
                       override val displayName: String) : TyClass(displayName), ITyPrimitive {

    override val kind = TyKind.Primitive

    override fun getSuperClass(context: SearchContext): ITy? = null

    override fun doLazyInit(searchContext: SearchContext) { }

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {
        // Everything is subset of any
        if (other.kind == TyKind.Unknown) return !strict

        // Handle unions, subtype if subtype of any of the union components.
        if (other is TyUnion) return other.getChildTypes().any { type -> subTypeOf(type, context, strict) }

        return this == other
    }

    override fun equals(other: Any?): Boolean {
        return other is TyPrimitiveClass && other.primitiveKind == primitiveKind
    }

    override fun hashCode(): Int {
        return primitiveKind.hashCode()
    }
}