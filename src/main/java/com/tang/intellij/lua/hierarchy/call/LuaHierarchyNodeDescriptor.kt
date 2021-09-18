

package com.tang.intellij.lua.hierarchy.call

import com.intellij.icons.AllIcons
import com.intellij.ide.IdeBundle
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor
import com.intellij.openapi.roots.ui.util.CompositeAppearance
import com.intellij.openapi.util.Comparing
import com.intellij.pom.Navigatable
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.tang.intellij.lua.psi.LuaLocalFuncDef

class LuaHierarchyNodeDescriptor(parentDescriptor: HierarchyNodeDescriptor?, element: PsiElement, isBase: Boolean)
    : HierarchyNodeDescriptor(element.project, parentDescriptor, element, isBase), Navigatable {

    override fun navigate(requestFocus: Boolean) {
        val element = psiElement
        if (element is Navigatable && element.canNavigate()) {
            element.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean {
        return psiElement is Navigatable
    }

    override fun canNavigateToSource(): Boolean {
        return psiElement is Navigatable
    }

    override fun update(): Boolean {
        var changes = super.update()
        val oldText = myHighlightedText

        myHighlightedText = CompositeAppearance()

        val element = psiElement as NavigatablePsiElement?
        if (element == null) {
            val invalidPrefix = IdeBundle.message("node.hierarchy.invalid")
            if (!myHighlightedText.text.startsWith(invalidPrefix)) {
                myHighlightedText.beginning.addText(invalidPrefix, HierarchyNodeDescriptor.getInvalidPrefixAttributes())
            }
            return true
        }

        val presentation = element.presentation
        if (presentation != null) {
            myHighlightedText.ending.addText(presentation.presentableText)
            myHighlightedText.ending.addText(" " + presentation.locationString, HierarchyNodeDescriptor.getPackageNameAttributes())
            icon = presentation.getIcon(false)
        } else {
            if (element is LuaLocalFuncDef) {
                myHighlightedText.ending.addText(element.name ?: "")
                myHighlightedText.ending.addText(" " + element.containingFile.name, HierarchyNodeDescriptor.getPackageNameAttributes())
                icon = AllIcons.Nodes.Function
            }
        }
        myName = myHighlightedText.text

        if (!Comparing.equal(myHighlightedText, oldText)) {
            changes = true
        }
        return changes
    }
}