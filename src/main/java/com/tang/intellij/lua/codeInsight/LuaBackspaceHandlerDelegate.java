

package com.tang.intellij.lua.codeInsight;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.tang.intellij.lua.comment.psi.LuaDocTypes;
import com.tang.intellij.lua.comment.psi.api.LuaComment;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by tangzx on 2016/12/28.
 */
public class LuaBackspaceHandlerDelegate extends BackspaceHandlerDelegate {
    @Override
    public void beforeCharDeleted(char c, @NotNull PsiFile psiFile, @NotNull Editor editor) {

    }

    @Override
    public boolean charDeleted(char c, @NotNull PsiFile psiFile, @NotNull Editor editor) {
        if (c == '-') { // 一口气删了 ---
            int offset = editor.getCaretModel().getOffset();
            PsiElement element = psiFile.findElementAt(offset);
            if (element != null) {
                ASTNode node = element.getNode();
                IElementType type = node.getElementType();
                if (type == LuaDocTypes.DASHES) {
                    int end = node.getStartOffset() + node.getTextLength();
                    int start = node.getStartOffset();
                    if (offset == end - 1 && node.getTextLength() == 3) { //确保是在 --- 后面删的

                        LuaComment comment = PsiTreeUtil.getParentOfType(element, LuaComment.class);
                        assert comment != null;

                        //if (comment.getNode().getStartOffset() < start) //如果有两行以上的 --- 则把换行符一起删了
                        //    start = start - 1;

                        editor.getDocument().deleteString(start, offset);
                        editor.getCaretModel().moveToOffset(start);
                    }
                }
            }
        }
        return false;
    }
}
