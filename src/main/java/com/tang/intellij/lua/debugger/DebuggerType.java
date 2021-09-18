

package com.tang.intellij.lua.debugger;


public enum DebuggerType {
    Attach(1, "Attach Debugger(Not available)"), Mob(2, "Remote Debugger(Mobdebug)");
    private int v;
    private String desc;
    DebuggerType(int v, String desc) {
        this.v = v;
        this.desc = desc;
    }
    public static DebuggerType valueOf(int v) {
        switch (v) {
            case 1: return Attach;
            case 2: return Mob;
            default: return null;
        }
    }
    public int value() { return v; }
    public String toString() { return desc; }
}
