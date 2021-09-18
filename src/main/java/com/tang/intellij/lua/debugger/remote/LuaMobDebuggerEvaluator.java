

package com.tang.intellij.lua.debugger.remote;

import com.intellij.xdebugger.XSourcePosition;
import com.tang.intellij.lua.debugger.LuaDebuggerEvaluator;
import com.tang.intellij.lua.debugger.remote.commands.EvaluatorCommand;
import com.tang.intellij.lua.debugger.remote.value.LuaRValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 *
 * First Created on 2016/12/31.
 */
public class LuaMobDebuggerEvaluator extends LuaDebuggerEvaluator {
    private LuaMobDebugProcess process;
    private LuaMobStackFrame stackFrame;

    public LuaMobDebuggerEvaluator(@NotNull LuaMobDebugProcess process, @NotNull LuaMobStackFrame stackFrame) {
        this.process = process;
        this.stackFrame = stackFrame;
    }

    @Override
    protected void eval(@NotNull String s, @NotNull XEvaluationCallback xEvaluationCallback, @Nullable XSourcePosition xSourcePosition) {
        EvaluatorCommand evaluatorCommand = new EvaluatorCommand("return " + s, this.stackFrame.getStackLevel(), data -> {
            Globals standardGlobals = JsePlatform.standardGlobals();
            LuaValue code = standardGlobals.load(data);
            code = code.call();

            String code2Str = code.get(1).toString();
            LuaValue code2 = standardGlobals.load(String.format("local _=%s return _", code2Str));

            LuaRValue value = LuaRValue.Companion.create(s, code2.call(), s, process.getSession());

            xEvaluationCallback.evaluated(value);
        });
        process.runCommand(evaluatorCommand);
    }
}
