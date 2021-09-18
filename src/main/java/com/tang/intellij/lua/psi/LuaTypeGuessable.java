

package com.tang.intellij.lua.psi;

import com.tang.intellij.lua.search.SearchContext;
import com.tang.intellij.lua.ty.ITy;
import com.tang.intellij.lua.ty.TyAliasSubstitutor;

/**
 *
 * First Created on 2016/12/1.
 */
public interface LuaTypeGuessable extends LuaPsiElement {
    default ITy guessType(SearchContext context) {
        ITy ty = SearchContext.Companion.infer(this, context);
        ty = TyAliasSubstitutor.Companion.substitute(ty, context);
        return ty;
    }
}