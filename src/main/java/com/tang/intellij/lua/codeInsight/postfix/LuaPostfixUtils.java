

package com.tang.intellij.lua.codeInsight.postfix;

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelector;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateExpressionSelectorBase;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.tang.intellij.lua.psi.LuaExpr;
import com.tang.intellij.lua.psi.LuaExprStat;
import com.tang.intellij.lua.psi.LuaParenExpr;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LuaPostfixUtils {

    public static final Condition<PsiElement> IS_NON_PAR = (element) -> element instanceof LuaExpr && !(element instanceof LuaParenExpr);

    public static PostfixTemplateExpressionSelector selectorTopmost() {
        return selectorTopmost(Conditions.alwaysTrue());
    }

    public static PostfixTemplateExpressionSelector selectorTopmost(Condition<PsiElement> additionalFilter) {
        return new PostfixTemplateExpressionSelectorBase(additionalFilter) {
            @Override
            protected List<PsiElement> getNonFilteredExpressions(@NotNull PsiElement psiElement, @NotNull Document document, int i) {
                LuaExprStat stat = PsiTreeUtil.getNonStrictParentOfType(psiElement, LuaExprStat.class);
                PsiElement expr = null;
                if (stat != null) {
                    expr = stat.getExpr();
                }

                return ContainerUtil.createMaybeSingletonList(expr);
            }
        };
    }

    public static PostfixTemplateExpressionSelector selectorAllExpressionsWithCurrentOffset() {
        return selectorAllExpressionsWithCurrentOffset(Conditions.alwaysTrue());
    }

    public static PostfixTemplateExpressionSelector selectorAllExpressionsWithCurrentOffset(final Condition<PsiElement> additionalFilter) {
        return new PostfixTemplateExpressionSelectorBase(additionalFilter) {
            @Override
            protected List<PsiElement> getNonFilteredExpressions(@NotNull PsiElement psiElement, @NotNull Document document, int i) {
                LuaExpr expr = PsiTreeUtil.getNonStrictParentOfType(psiElement, LuaExpr.class);
                List<PsiElement> list = new SmartList<>();
                while (expr != null) {
                    if (!PsiTreeUtil.hasErrorElements(expr))
                        list.add(expr);
                    expr = PsiTreeUtil.getParentOfType(expr, LuaExpr.class);
                }
                return list;
            }
        };
    }
}
