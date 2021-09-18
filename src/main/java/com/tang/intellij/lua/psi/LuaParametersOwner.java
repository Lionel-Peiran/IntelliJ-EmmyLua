

package com.tang.intellij.lua.psi;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * function
 * for
 * First Created on 2016/12/21.
 */
public interface LuaParametersOwner extends LuaPsiElement {
    @Nullable
    List<LuaParamNameDef> getParamNameDefList();
}
