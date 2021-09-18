

package com.intellij.codeInsight.daemon

import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import java.util.function.Supplier
import javax.swing.Icon

fun <T : PsiElement> createLineMarkerInfo(
    element: T,
    range: TextRange,
    icon: Icon,
    tooltipProvider: Function<in T, String>?,
    navigationHandler: GutterIconNavigationHandler<T>?,
    alignment: GutterIconRenderer.Alignment,
    accessibleNameProvider: Supplier<String>
): LineMarkerInfo<T> {
    return LineMarkerInfo(
        element,
        range,
        icon,
        tooltipProvider,
        navigationHandler,
        alignment,
        accessibleNameProvider
    )
}