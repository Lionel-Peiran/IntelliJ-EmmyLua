

package com.tang.intellij.lua.psi;

import com.tang.intellij.lua.search.SearchContext;
import com.tang.intellij.lua.ty.ITy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * #local function
 * #function
 * #lambda function
 * #class method
 *
 * First Created on 2016/12/9.
 */
public interface LuaFuncBodyOwner extends LuaParametersOwner, LuaTypeGuessable {
    @Nullable
    LuaFuncBody getFuncBody();

    /**
     * 返回类型
     */
    @NotNull
    ITy guessReturnType(SearchContext searchContext);

    @Nullable
    default ITy getVarargType() {
        return LuaPsiImplUtilKt.getVarargTy(this);
    }

    @NotNull
    LuaParamInfo[] getParams();

    default String getParamSignature() {
        return LuaPsiImplUtilKt.getParamSignature(this);
    }
}
