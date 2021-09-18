

package com.tang.intellij.lua.codeInsight.postfix.templates;

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.psi.PsiElement;
import com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorAllExpressionsWithCurrentOffset;

/**
 * expr -> (expr)
 * Created by TangZX on 2017/4/25.
 */
public class LuaParPostfixTemplate extends StringBasedPostfixTemplate {
    public LuaParPostfixTemplate() {
        super("par", "(expr)", selectorAllExpressionsWithCurrentOffset(LuaPostfixUtils.IS_NON_PAR));
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement psiElement) {
        return "($expr$)$END$";
    }

    @Override
    protected PsiElement getElementToRemove(PsiElement expr) {
        return expr;
    }
}