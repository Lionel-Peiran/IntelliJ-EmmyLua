

@file:Suppress("MemberVisibilityCanBePrivate")

package com.tang.intellij.lua.debugger.emmy

import com.google.gson.Gson

enum class MessageCMD {
    Unknown,

    InitReq,
    InitRsp,

    ReadyReq,
    ReadyRsp,

    AddBreakPointReq,
    AddBreakPointRsp,

    RemoveBreakPointReq,
    RemoveBreakPointRsp,

    ActionReq,
    ActionRsp,

    EvalReq,
    EvalRsp,

    // lua -> ide
    BreakNotify,
    AttachedNotify,

    StartHookReq,
    StartHookRsp,

    LogNotify,
}

interface IMessage {
    val cmd: Int
    fun toJSON(): String
}

open class Message(cmdName: MessageCMD) : IMessage {
    override val cmd = cmdName.ordinal

    override fun toJSON(): String {
        return Gson().toJson(this)
    }

    companion object {
        private var seqCount = 0

        fun makeSeq(): Int {
            return seqCount++
        }
    }
}

class InitMessage(val emmyHelper: String, val ext: Array<String>) : Message(MessageCMD.InitReq)

enum class DebugAction {
    Break,
    Continue,
    StepOver,
    StepIn,
    StepOut,
    Stop,
}

class DebugActionMessage(actionName: DebugAction) : Message(MessageCMD.ActionReq) {
    val action = actionName.ordinal
}

enum class LuaValueType {
    TNIL,
    TBOOLEAN,
    TLIGHTUSERDATA,
    TNUMBER,
    TSTRING,
    TTABLE,
    TFUNCTION,
    TUSERDATA,
    TTHREAD,

    GROUP,
}

class VariableValue(val name: String,
                    val nameType: Int,
                    val value: String,
                    val valueType: Int,
                    val valueTypeName: String,
                    val cacheId: Int,
                    val children: List<VariableValue>?) {
    val nameTypeValue: LuaValueType get() {
        return LuaValueType.values().find { it.ordinal == nameType } ?: LuaValueType.TSTRING
    }

    val nameValue: String get() {
        if (nameTypeValue == LuaValueType.TSTRING)
            return name
        return "[$name]"
    }

    val valueTypeValue: LuaValueType get() {
        return LuaValueType.values().find { it.ordinal == valueType } ?: LuaValueType.TSTRING
    }

    val fake: Boolean get() {
        return valueTypeValue > LuaValueType.TTHREAD
    }
}

class Stack(
        val file: String,
        val line: Int,
        val functionName: String,
        val level: Int,
        val localVariables: List<VariableValue>,
        val upvalueVariables: List<VariableValue>
)

class BreakNotify(val stacks: List<Stack>)

class EvalReq(val expr: String, val stackLevel: Int, val cacheId: Int, val depth: Int) : Message(MessageCMD.EvalReq) {
    val seq = makeSeq()
}

class EvalRsp(val seq: Int, val success: Boolean, val error: String?, val value: VariableValue?)

class BreakPoint(val file: String, val line: Int)

class AddBreakPointReq(val breakPoints: List<BreakPoint>) : Message(MessageCMD.AddBreakPointReq)

class RemoveBreakPointReq(val breakPoints: List<BreakPoint>) : Message(MessageCMD.RemoveBreakPointReq)

class LogNotify(val type: Int, val message: String)

class AttachedNotify(val state: Long)