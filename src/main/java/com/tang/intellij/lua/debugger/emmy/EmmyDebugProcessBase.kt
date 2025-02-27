

package com.tang.intellij.lua.debugger.emmy

import com.google.gson.Gson
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.frame.XSuspendContext
import com.intellij.xdebugger.impl.XDebugSessionImpl
import com.tang.intellij.lua.debugger.*
import com.tang.intellij.lua.psi.LuaFileManager
import com.tang.intellij.lua.psi.LuaFileUtil
import java.io.File

abstract class EmmyDebugProcessBase(session: XDebugSession) : LuaDebugProcess(session), ITransportHandler {
    private val editorsProvider = LuaDebuggerEditorsProvider()
    private val evalHandlers = mutableListOf<IEvalResultHandler>()
    protected var transporter: Transporter? = null

    override fun sessionInitialized() {
        super.sessionInitialized()
        ApplicationManager.getApplication().executeOnPooledThread {
            setupTransporter()
        }
    }

    protected abstract fun setupTransporter()

    private fun sendInitReq() {
        // send init
        val path = LuaFileUtil.getPluginVirtualFile("debugger/emmy/emmyHelper.lua")
        if (path != null) {
            val code = File(path).readText()
            val extList = LuaFileManager.getInstance().extensions
            transporter?.send(InitMessage(code, extList))
        }
        // send bps
        val breakpoints = XDebuggerManager.getInstance(session.project)
                .breakpointManager
                .getBreakpoints(LuaLineBreakpointType::class.java)
        breakpoints.forEach { breakpoint ->
            breakpoint.sourcePosition?.let { position ->
                registerBreakpoint(position, breakpoint)
            }
        }
        // send ready
        transporter?.send(Message(MessageCMD.ReadyReq))
    }

    override fun onConnect(suc: Boolean) {
        if (suc) {
            ApplicationManager.getApplication().runReadAction {
                sendInitReq()
            }
        }
        else stop()
    }

    override fun onDisconnect() {
        stop()
        session?.stop()
    }

    override fun onReceiveMessage(cmd: MessageCMD, json: String) {
        when (cmd) {
            MessageCMD.BreakNotify -> {
                val data = Gson().fromJson(json, BreakNotify::class.java)
                onBreak(data)
            }
            MessageCMD.EvalRsp -> {
                val rsp = Gson().fromJson(json, EvalRsp::class.java)
                onEvalRsp(rsp)
            }
            MessageCMD.LogNotify -> {
                val notify = Gson().fromJson(json, LogNotify::class.java)
                println(notify.message, LogConsoleType.NORMAL, ConsoleViewContentType.SYSTEM_OUTPUT)
            }
            else -> {
                println("Unknown message: $cmd")
            }
        }
    }

    override fun registerBreakpoint(sourcePosition: XSourcePosition, breakpoint: XLineBreakpoint<*>) {
        val file = sourcePosition.file
        val shortPath = file.canonicalPath
        if (shortPath != null) {
            send(AddBreakPointReq(listOf(BreakPoint(shortPath, breakpoint.line + 1))))
        }
    }

    override fun unregisterBreakpoint(sourcePosition: XSourcePosition, breakpoint: XLineBreakpoint<*>) {
        val file = sourcePosition.file
        val shortPath = file.canonicalPath
        if (shortPath != null) {
            send(RemoveBreakPointReq(listOf(BreakPoint(shortPath, breakpoint.line + 1))))
        }
    }

    override fun startPausing() {
        send(DebugActionMessage(DebugAction.Break))
    }

    private fun onBreak(data: BreakNotify) {
        evalHandlers.clear()
        val frames = data.stacks.map { EmmyDebugStackFrame(it, this) }
        val top = frames.firstOrNull { it.sourcePosition != null }
                ?: frames.firstOrNull { it.data.line > 0 }
                ?: frames.firstOrNull()
        val stack = LuaExecutionStack(frames)
        if (top != null)
            stack.setTopFrame(top)
        val breakpoint = top?.sourcePosition?.let { getBreakpoint(it.file, it.line) }
        if (breakpoint != null) {
            ApplicationManager.getApplication().invokeLater {
                session.breakpointReached(breakpoint, null, LuaSuspendContext(stack))
                session.showExecutionPoint()
            }
        } else {
            ApplicationManager.getApplication().invokeLater {
                val se = session
                if (se is XDebugSessionImpl)
                    se.positionReached(LuaSuspendContext(stack), true)
                else
                    se.positionReached(LuaSuspendContext(stack))
                session.showExecutionPoint()
            }
        }
    }

    private fun onEvalRsp(rsp: EvalRsp) {
        evalHandlers.forEach { it.handleMessage(rsp) }
    }

    override fun run() {
        send(DebugActionMessage(DebugAction.Continue))
    }

    override fun stop() {
        send(DebugActionMessage(DebugAction.Stop))
        send(StopSign())
        transporter?.close()
        transporter = null
    }

    override fun startStepOver(context: XSuspendContext?) {
        send(DebugActionMessage(DebugAction.StepOver))
    }

    override fun startStepInto(context: XSuspendContext?) {
        send(DebugActionMessage(DebugAction.StepIn))
    }

    override fun startStepOut(context: XSuspendContext?) {
        send(DebugActionMessage(DebugAction.StepOut))
    }

    override fun getEditorsProvider(): XDebuggerEditorsProvider {
        return editorsProvider
    }

    fun addEvalResultHandler(handler: IEvalResultHandler) {
        evalHandlers.add(handler)
    }

    fun removeMessageHandler(handler: IEvalResultHandler) {
        evalHandlers.remove(handler)
    }

    fun send(msg: IMessage) {
        transporter?.send(msg)
    }
}