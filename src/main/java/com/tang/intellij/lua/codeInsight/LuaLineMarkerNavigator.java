

package com.tang.intellij.lua.codeInsight;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.util.Query;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by tangzx on 2017/3/30.
 */
public abstract class LuaLineMarkerNavigator<T extends PsiElement, S extends PsiElement> implements GutterIconNavigationHandler<T> {
    @Override
    public void navigate(MouseEvent mouseEvent, T t) {
        final List<NavigatablePsiElement> navElements = new ArrayList<>();
        Query<S> search = search(t);
        if (search != null) {
            search.forEach(t1 -> {
                navElements.add((NavigatablePsiElement) t1);
            });
            PsiElementListNavigator.openTargets(mouseEvent,
                    navElements.toArray(new NavigatablePsiElement[0]),
                    getTitle(t),
                    null,
                    new DefaultPsiElementCellRenderer());
        }
    }

    protected abstract String getTitle(T elt);

    @Nullable
    protected abstract Query<S> search(T elt);
}
