

package com.tang.intellij.lua.ty

import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.Processor
import com.intellij.util.containers.ContainerUtil
import com.tang.intellij.lua.Constants
import com.tang.intellij.lua.project.LuaSettings
import com.tang.intellij.lua.search.SearchContext

enum class TyKind {
    Unknown,
    Primitive,
    Array,
    Function,
    Class,
    Union,
    Generic,
    Nil,
    Void,
    Tuple,
    GenericParam,
    StringLiteral
}
enum class TyPrimitiveKind {
    String,
    Number,
    Boolean,
    Table,
    Function
}
class TyFlags {
    companion object {
        const val ANONYMOUS = 0x1
        const val GLOBAL = 0x2
        const val SELF_FUNCTION = 0x4 // xxx.method()
        const val ANONYMOUS_TABLE = 0x8 // local xx = {}, flag of this table `{}`
    }
}

interface ITy : Comparable<ITy> {
    val kind: TyKind

    val displayName: String

    val flags: Int

    fun union(ty: ITy): ITy

    fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean

    fun getSuperClass(context: SearchContext): ITy?

    fun visitSuper(searchContext: SearchContext, processor: Processor<ITyClass>)

    fun substitute(substitutor: ITySubstitutor): ITy

    fun each(fn: (ITy) -> Unit) {
        TyUnion.each(this, fn)
    }

    fun eachTopClass(fn: Processor<ITyClass>)

    fun accept(visitor: ITyVisitor)

    fun acceptChildren(visitor: ITyVisitor)
}

fun ITy.hasFlag(flag: Int): Boolean = flags and flag == flag

val ITy.isGlobal: Boolean
    get() = hasFlag(TyFlags.GLOBAL)

val ITy.isAnonymous: Boolean
    get() = hasFlag(TyFlags.ANONYMOUS)

private val ITy.worth: Float get() {
    var value = 10f
    when(this) {
        is ITyArray, is ITyGeneric -> value = 80f
        is ITyPrimitive -> value = 70f
        is ITyFunction -> value = 60f
        is ITyClass -> {
            value = when {
                this is TyTable -> 9f
                this.isAnonymous -> 2f
                this.isGlobal -> 5f
                else -> 90f
            }
        }
    }
    return value
}

abstract class Ty(override val kind: TyKind) : ITy {

    final override var flags: Int = 0

    override val displayName: String
        get() = TyRenderer.SIMPLE.render(this)

    fun addFlag(flag: Int) {
        flags = flags or flag
    }

    override fun accept(visitor: ITyVisitor) {
        visitor.visitTy(this)
    }

    override fun acceptChildren(visitor: ITyVisitor) {
    }

    override fun union(ty: ITy): ITy {
        return TyUnion.union(this, ty)
    }

    override fun toString(): String {
        val list = mutableListOf<String>()
        TyUnion.each(this) { //尽量不使用Global
            if (!it.isAnonymous && !(it is ITyClass && it.isGlobal))
                list.add(it.displayName)
        }
        if (list.isEmpty()) { //使用Global
            TyUnion.each(this) {
                if (!it.isAnonymous && (it is ITyClass && it.isGlobal))
                    list.add(it.displayName)
            }
        }
        return list.joinToString("|")
    }

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {
        // Everything is subset of any
        if (other.kind == TyKind.Unknown) return !strict

        // Handle unions, subtype if subtype of any of the union components.
        if (other is TyUnion) return other.getChildTypes().any { type -> subTypeOf(type, context, strict) }

        // Classes are equal
        return this == other
    }

    override fun getSuperClass(context: SearchContext): ITy? {
        return null
    }

    override fun visitSuper(searchContext: SearchContext, processor: Processor<ITyClass>) {
        val superType = getSuperClass(searchContext) as? ITyClass ?: return
        if (processor.process(superType))
            superType.visitSuper(searchContext, processor)
    }

    override fun compareTo(other: ITy): Int {
        return other.worth.compareTo(worth)
    }

    override fun substitute(substitutor: ITySubstitutor): ITy {
        return substitutor.substitute(this)
    }

    override fun eachTopClass(fn: Processor<ITyClass>) {
        when (this) {
            is ITyClass -> fn.process(this)
            is TyUnion -> {
                ContainerUtil.process(getChildTypes()) {
                    if (it is ITyClass && !fn.process(it))
                        return@process false
                    true
                }
            }
            is TyTuple -> {
                list.firstOrNull()?.eachTopClass(fn)
            }
        }
    }

    companion object {

        val UNKNOWN = TyUnknown()
        val VOID = TyVoid()
        val BOOLEAN = TyPrimitive(TyPrimitiveKind.Boolean, "boolean")
        val STRING = TyPrimitiveClass(TyPrimitiveKind.String, "string")
        val NUMBER = TyPrimitive(TyPrimitiveKind.Number, "number")
        val TABLE = TyPrimitive(TyPrimitiveKind.Table, "table")
        val FUNCTION = TyPrimitive(TyPrimitiveKind.Function, "function")
        val NIL = TyNil()

        private val serializerMap = mapOf<TyKind, ITySerializer>(
                TyKind.Array to TyArraySerializer,
                TyKind.Class to TyClassSerializer,
                TyKind.Function to TyFunctionSerializer,
                TyKind.Generic to TyGenericSerializer,
                TyKind.GenericParam to TyGenericParamSerializer,
                TyKind.StringLiteral to TyStringLiteralSerializer,
                TyKind.Tuple to TyTupleSerializer,
                TyKind.Union to TyUnionSerializer
        )

        private fun getPrimitive(mark: Byte): Ty {
            return when (mark.toInt()) {
                TyPrimitiveKind.Boolean.ordinal -> BOOLEAN
                TyPrimitiveKind.String.ordinal -> STRING
                TyPrimitiveKind.Number.ordinal -> NUMBER
                TyPrimitiveKind.Table.ordinal -> TABLE
                TyPrimitiveKind.Function.ordinal -> FUNCTION
                else -> UNKNOWN
            }
        }

        private fun getKind(ordinal: Int): TyKind {
            return TyKind.values().firstOrNull { ordinal == it.ordinal } ?: TyKind.Unknown
        }

        fun getBuiltin(name: String): ITy? {
            return when (name) {
                Constants.WORD_NIL -> NIL
                Constants.WORD_VOID -> VOID
                Constants.WORD_ANY -> UNKNOWN
                Constants.WORD_BOOLEAN -> BOOLEAN
                Constants.WORD_STRING -> STRING
                Constants.WORD_NUMBER -> NUMBER
                Constants.WORD_TABLE -> TABLE
                Constants.WORD_FUNCTION -> FUNCTION
                else -> null
            }
        }

        fun create(name: String): ITy {
            return getBuiltin(name) ?: TyLazyClass(name)
        }

        fun isInvalid(ty: ITy?): Boolean {
            return ty == null || ty is TyUnknown || ty is TyNil || ty is TyVoid
        }

        private fun getSerializer(kind: TyKind): ITySerializer? {
            return serializerMap[kind]
        }

        fun serialize(ty: ITy, stream: StubOutputStream) {
            stream.writeByte(ty.kind.ordinal)
            stream.writeInt(ty.flags)
            when(ty) {
                is ITyPrimitive -> stream.writeByte(ty.primitiveKind.ordinal)
                else -> {
                    val serializer = getSerializer(ty.kind)
                    serializer?.serialize(ty, stream)
                }
            }
        }

        fun deserialize(stream: StubInputStream): ITy {
            val kind = getKind(stream.readByte().toInt())
            val flags = stream.readInt()
            return when (kind) {
                TyKind.Primitive -> getPrimitive(stream.readByte())
                TyKind.Nil -> NIL
                TyKind.Void -> VOID
                else -> {
                    val serializer = getSerializer(kind)
                    serializer?.deserialize(flags, stream) ?: UNKNOWN
                }
            }
        }
    }
}

class TyUnknown : Ty(TyKind.Unknown) {

    override fun equals(other: Any?): Boolean {
        return other is TyUnknown
    }

    override fun hashCode(): Int {
        return Constants.WORD_ANY.hashCode()
    }

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {
        return !strict
    }
}

class TyNil : Ty(TyKind.Nil) {

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {

        return super.subTypeOf(other, context, strict) || other is TyNil || !LuaSettings.instance.isNilStrict
    }
}

class TyVoid : Ty(TyKind.Void) {

    override fun subTypeOf(other: ITy, context: SearchContext, strict: Boolean): Boolean {
        return false
    }
}