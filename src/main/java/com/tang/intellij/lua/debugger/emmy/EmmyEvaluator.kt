

package com.tang.intellij.lua.debugger.emmy

import com.intellij.xdebugger.XSourcePosition
import com.tang.intellij.lua.debugger.LuaDebuggerEvaluator
import com.tang.intellij.lua.debugger.emmy.value.LuaXValue

class EmmyEvaluator(val frame: EmmyDebugStackFrame, val process: EmmyDebugProcessBase) : LuaDebuggerEvaluator(), IEvalResultHandler {

    private val callbackMap = mutableMapOf<Int, XEvaluationCallback>()

    init {
        process.addEvalResultHandler(this)
    }

    override fun handleMessage(msg: EvalRsp) {
        val callback = callbackMap[msg.seq]
        if (callback != null) {
            if (msg.success)
                callback.evaluated(LuaXValue.create(msg.value!!, frame))
            else
                callback.errorOccurred(msg.error ?: "unknown error")
            callbackMap.remove(msg.seq)
        }
    }

    fun eval(express: String, cacheId: Int, xEvaluationCallback: XEvaluationCallback, depth: Int = 1) {
        val req = EvalReq(express, frame.data.level, cacheId, depth)
        process.send(req)
        callbackMap[req.seq] = xEvaluationCallback
    }

    override fun eval(express: String, xEvaluationCallback: XEvaluationCallback, xSourcePosition: XSourcePosition?) {
        eval(express, 0, xEvaluationCallback)
    }
}