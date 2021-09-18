

package com.tang.intellij.lua.debugger.remote;

import com.intellij.ui.ColoredTextContainer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XNamedValue;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.tang.intellij.lua.lang.LuaIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by tangzx on 2016/12/31.
 */
public class LuaMobStackFrame extends XStackFrame {

    private String functionName;
    private XSourcePosition position;
    private LuaMobDebugProcess process;
    private XValueChildrenList values = new XValueChildrenList();
    private int stackLevel = 0;

    public LuaMobStackFrame(String functionName, XSourcePosition position, int _stackLevel, LuaMobDebugProcess debugProcess) {
        this.functionName = functionName;
        this.position = position;
        this.stackLevel = _stackLevel;
        process = debugProcess;
    }

    @Nullable
    @Override
    public XDebuggerEvaluator getEvaluator() {
        return  new LuaMobDebuggerEvaluator(process, this);
    }

    @Nullable
    @Override
    public XSourcePosition getSourcePosition() {
        return position;
    }

    public void addValue(XNamedValue namedValue) {
        values.add(namedValue);
    }

    @Override
    public void computeChildren(@NotNull XCompositeNode node) {
        node.addChildren(values, true);
    }

    public void customizePresentation(@NotNull ColoredTextContainer component) {
        XSourcePosition position = this.getSourcePosition();
        String info = functionName;
        String positionInfo = "unknown";
        if(position != null) {
            positionInfo = position.getFile().getName() + ":" + (position.getLine() + 1);
        }

        if (functionName != null)
            info = String.format("%s (%s)", functionName, positionInfo);
        component.append(info, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        component.setIcon(LuaIcons.Debugger.StackFrame);
    }

    public int getStackLevel()
    {
        return this.stackLevel;
    }
}
