

package com.tang.intellij.lua.codeInsight.postfix.templates;

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorTopmost;

/**
 * for i = 1, n do end
 * First Created on 2017/2/5.
 */
public class LuaForAPostfixTemplate extends StringBasedPostfixTemplate {
    public LuaForAPostfixTemplate() {
        super("for", "for i = 1, expr do end", selectorTopmost(Conditions.alwaysTrue()));
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement psiElement) {
        return "for i = 1, $expr$ do\n$END$\nend";
    }

    @Override
    protected PsiElement getElementToRemove(PsiElement expr) {
        return expr;
    }
}
