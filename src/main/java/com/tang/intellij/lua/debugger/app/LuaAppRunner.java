

package com.tang.intellij.lua.debugger.app;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.tang.intellij.lua.debugger.LuaRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by tangzx on 2017/5/7.
 */
public class LuaAppRunner extends LuaRunner {
    private static final String ID = "lua.app.runner";
    @NotNull
    @Override
    public String getRunnerId() {
        return ID;
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return runProfile instanceof LuaAppRunConfiguration && super.canRun(s, runProfile);
    }

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        FileDocumentManager.getInstance().saveAllDocuments();

        // debug
        if (environment.getExecutor().getId().equals(DefaultDebugExecutor.EXECUTOR_ID)) {
            XDebugSession session = createSession(environment);
            return session.getRunContentDescriptor();
        }

        // execute
        ExecutionResult result = state.execute(environment.getExecutor(), environment.getRunner());
        if (result != null) {
            RunContentBuilder builder = new RunContentBuilder(result, environment);
            return builder.showRunContent(environment.getContentToReuse());
        }
        return null;
    }

    private XDebugSession createSession(ExecutionEnvironment environment) throws ExecutionException {
        XDebuggerManager manager = XDebuggerManager.getInstance(environment.getProject());
        return manager.startSession(environment, new XDebugProcessStarter() {
            @NotNull
            @Override
            public XDebugProcess start(@NotNull XDebugSession xDebugSession) throws ExecutionException {
                LuaAppRunConfiguration configuration = (LuaAppRunConfiguration) environment.getRunProfile();
                return new LuaAppMobProcess(xDebugSession);
            }
        });
    }
}
