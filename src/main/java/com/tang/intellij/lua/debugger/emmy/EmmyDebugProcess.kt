

package com.tang.intellij.lua.debugger.emmy

import com.intellij.xdebugger.XDebugSession

interface IEvalResultHandler {
    fun handleMessage(msg: EvalRsp)
}

open class EmmyDebugProcess(session: XDebugSession) : EmmyDebugProcessBase(session), ITransportHandler {
    private val configuration = session.runProfile as EmmyDebugConfiguration

    override fun setupTransporter() {
        val transporter: Transporter = when (configuration.type) {
            EmmyDebugTransportType.PIPE_CLIENT -> PipelineClientTransporter(configuration.pipeName)
            EmmyDebugTransportType.PIPE_SERVER -> PipelineServerTransporter(configuration.pipeName)
            EmmyDebugTransportType.TCP_CLIENT -> SocketClientTransporter(configuration.host, configuration.port)
            EmmyDebugTransportType.TCP_SERVER -> SocketServerTransporter(configuration.host, configuration.port)
        }
        transporter.handler = this
        transporter.logger = this
        this.transporter = transporter
        try {
            transporter.start()
        } catch (e: Exception) {
            this.error(e.localizedMessage)
            this.onDisconnect()
        }
    }
}