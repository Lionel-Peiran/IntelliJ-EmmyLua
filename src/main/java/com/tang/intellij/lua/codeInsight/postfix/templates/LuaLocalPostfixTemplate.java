

package com.tang.intellij.lua.codeInsight.postfix.templates;

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateWithExpressionSelector;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.tang.intellij.lua.psi.LuaExpr;
import com.tang.intellij.lua.refactoring.rename.LuaIntroduceVarHandler;
import org.jetbrains.annotations.NotNull;

import static com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorAllExpressionsWithCurrentOffset;

/**
 * post fix test
 * First Created on 2017/2/4.
 */
public class LuaLocalPostfixTemplate extends PostfixTemplateWithExpressionSelector {

    public LuaLocalPostfixTemplate() {
        super("var", "local inst = expr", selectorAllExpressionsWithCurrentOffset());
    }

    @Override
    protected void expandForChooseExpression(@NotNull PsiElement psiElement, @NotNull Editor editor) {
        LuaIntroduceVarHandler handler = new LuaIntroduceVarHandler();
        handler.invoke(psiElement.getProject(), editor, (LuaExpr) psiElement);
    }
}
