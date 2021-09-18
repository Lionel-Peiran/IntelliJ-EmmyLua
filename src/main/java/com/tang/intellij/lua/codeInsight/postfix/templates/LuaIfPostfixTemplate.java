

package com.tang.intellij.lua.codeInsight.postfix.templates;

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorTopmost;

/**
 * if expr then end
 * Created by tangzx on 2017/2/5.
 */
public class LuaIfPostfixTemplate extends StringBasedPostfixTemplate {
    public LuaIfPostfixTemplate() {
        super("if", "if expr then end", selectorTopmost());
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement psiElement) {
        return "if $expr$ then\n$END$\nend";
    }

    @Override
    protected PsiElement getElementToRemove(PsiElement expr) {
        return expr;
    }
}
