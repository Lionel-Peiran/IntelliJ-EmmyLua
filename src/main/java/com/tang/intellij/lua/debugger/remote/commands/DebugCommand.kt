

package com.tang.intellij.lua.debugger.remote.commands

import com.tang.intellij.lua.debugger.remote.LuaMobDebugProcess
import com.tang.intellij.lua.debugger.remote.MobClient

/**
 * Remote Debug Command
 * Created by tangzx on 2016/12/31.
 */
abstract class DebugCommand {

    lateinit var debugProcess: LuaMobDebugProcess

    abstract fun isFinished(): Boolean

    abstract fun getRequireRespLines(): Int

    abstract fun write(writer: MobClient)

    abstract fun handle(data: String): Int
}
