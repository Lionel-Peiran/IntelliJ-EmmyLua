

package com.tang.intellij.lua.debugger

import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.impl.frame.XStackFrameContainerEx

/**
 *
 * First Created on 2016/12/31.
 */
class LuaExecutionStack(private val stackFrameList: List<XStackFrame>) : XExecutionStack("LuaStack") {
    private var _topFrame: XStackFrame? = null

    val stackFrames: Array<XStackFrame>
        get() = stackFrameList.toTypedArray()

    init {
        if (stackFrameList.isNotEmpty())
            _topFrame = stackFrameList[0]
    }

    override fun getTopFrame() = _topFrame

    fun setTopFrame(frame: XStackFrame) {
        _topFrame = frame
    }

    override fun computeStackFrames(i: Int, xStackFrameContainer: XExecutionStack.XStackFrameContainer) {
        val stackFrameContainerEx = xStackFrameContainer as XStackFrameContainerEx
        stackFrameContainerEx.addStackFrames(stackFrameList, topFrame, true)
    }
}
