

package com.tang.intellij.lua.debugger.remote

import com.intellij.execution.ui.ConsoleViewContentType
import com.tang.intellij.lua.debugger.DebugLogger
import com.tang.intellij.lua.debugger.LogConsoleType
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel

interface MobServerListener : DebugLogger {
    fun onConnect(client: MobClient)
    fun handleResp(client: MobClient, code: Int, data: String?)
    fun onDisconnect(client: MobClient)
    val process: LuaMobDebugProcess
}

class MobServer(private val listener: MobServerListener) : Runnable {
    private var server: ServerSocketChannel? = null
    private var client: MobClient? = null
    private var port: Int = 0
    private var isStopped: Boolean = false

    @Throws(IOException::class)
    fun start(port: Int) {
        this.port = port
        if (server == null)
            server = ServerSocketChannel.open()
        server?.bind(InetSocketAddress(port))
        val thread = Thread(this)
        thread.start()
    }

    fun restart() {
        client?.stop()
        client = null
    }

    override fun run() {
        while (!isStopped) {
            try {
                val accept = server?.accept() ?: break
                client?.stop()
                listener.println("Connected.", LogConsoleType.NORMAL, ConsoleViewContentType.SYSTEM_OUTPUT)
                val newClient = MobClient(accept, listener)
                client = newClient
                listener.onConnect(newClient)
            } catch (e: IOException) {
                break
            }
        }
    }

    fun stop() {
        isStopped = true
        client?.stop()
        client = null
        try {
            server?.close()
        } catch (ignored: IOException) {

        }
    }
}
