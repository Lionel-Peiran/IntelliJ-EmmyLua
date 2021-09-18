

package com.tang.intellij.lua.psi;

import com.tang.intellij.lua.comment.psi.api.LuaComment;
import org.jetbrains.annotations.Nullable;

/**
 *
 * First Created on 2016/11/24.
 */
public interface LuaCommentOwner extends LuaPsiElement {
    @Nullable
    LuaComment getComment();
}
