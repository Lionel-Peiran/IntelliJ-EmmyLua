

package com.tang.intellij.lua.codeInsight.postfix.templates;

import com.intellij.codeInsight.template.postfix.templates.StringBasedPostfixTemplate;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.tang.intellij.lua.codeInsight.postfix.LuaPostfixUtils.selectorTopmost;

/**
 * for i, v in pairs(expr) do end
 * Created by tangzx on 2017/2/5.
 */
public class LuaForPPostfixTemplate extends StringBasedPostfixTemplate {
    public LuaForPPostfixTemplate() {
        super("for_p", "for i, v in pairs(expr) do end", selectorTopmost(Conditions.alwaysTrue()));
    }

    @Nullable
    @Override
    public String getTemplateString(@NotNull PsiElement psiElement) {
        return "for k, v in pairs($expr$) do\n$END$\nend";
    }

    @Override
    protected PsiElement getElementToRemove(PsiElement expr) {
        return expr;
    }
}
