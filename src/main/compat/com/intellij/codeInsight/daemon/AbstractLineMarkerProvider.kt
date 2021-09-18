

package com.intellij.codeInsight.daemon

import com.intellij.psi.PsiElement

abstract class AbstractLineMarkerProvider : LineMarkerProvider {
    override fun collectSlowLineMarkers(list: List<PsiElement>, collection: MutableCollection<in LineMarkerInfo<*>>) {
        collectSlowLineMarkersExt(list, collection)
    }

    abstract fun collectSlowLineMarkersExt(list: List<PsiElement>, collection: MutableCollection<in LineMarkerInfo<*>>)
}