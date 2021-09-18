

package com.tang.intellij.lua.debugger.remote.commands

import com.intellij.xdebugger.frame.XStackFrame
import com.tang.intellij.lua.debugger.LuaExecutionStack
import com.tang.intellij.lua.debugger.remote.LuaMobStackFrame
import com.tang.intellij.lua.debugger.remote.value.LuaRValue
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.JsePlatform
import java.util.*
import java.util.regex.Pattern

/**
 *
 * Created by tangzx on 2016/12/31.
 */
class GetStackCommand : DefaultCommand("STACK --{maxlevel=0}", 1) {

    private var hasError: Boolean = false
    private var errorDataLen: Int = 0

    override fun isFinished(): Boolean {
        return !hasError && super.isFinished()
    }

    override fun handle(data: String): Int {
        if (hasError) {
            hasError = false
            val error = data.substring(0, errorDataLen)
            debugProcess.error(error)
            debugProcess.runCommand(DefaultCommand("RUN", 0))
            return errorDataLen
        }
        return super.handle(data)
    }

    override fun handle(index: Int, data: String) {
        if (data.startsWith("401")) {
            hasError = true
            val pattern = Pattern.compile("(\\d+)([^\\d]+)(\\d+)")
            val matcher = pattern.matcher(data)
            if (matcher.find()) {
                errorDataLen = Integer.parseInt(matcher.group(3))
            }
            return
        }

        if (data.startsWith("200 OK")) {
            val stackCode = data.substring(6)
            val standardGlobals = JsePlatform.standardGlobals()
            val strippedCode = limitStringSize(stackCode)
            val code = standardGlobals.load(strippedCode)
            val function = code.checkfunction()
            val value = function.call()

            val frames = ArrayList<XStackFrame>()
            for (i in 1..value.length()) {
                val stackValue = value.get(i)
                val stackInfo = stackValue.get(1)

                val funcName = stackInfo.get(1)
                val fileName = stackInfo.get(2)
                val line = stackInfo.get(4)
                val position = debugProcess.findSourcePosition(fileName.toString(), line.toint())
                var functionName = funcName.toString()
                if (funcName.isnil())
                    functionName = "main"

                val frame = LuaMobStackFrame(functionName, position, i, debugProcess)

                parseValues(stackValue.get(2).checktable(), frame)
                parseValues(stackValue.get(3).checktable(), frame)

                frames.add(frame)
            }
            debugProcess.setStack(LuaExecutionStack(frames))
        }
    }

    private fun parseValues(paramsTable: LuaTable, frame: LuaMobStackFrame) {
        val keys = paramsTable.keys()
        for (key in keys) {
            val luaValue = paramsTable.get(key)
            val desc = luaValue.get(2)
            val xValue = LuaRValue.create(key.toString(), luaValue.get(1), desc.toString(), debugProcess.session)
            frame.addValue(xValue)
        }
    }
}