

package com.intellij.openapi.diagnostic

import com.intellij.util.Consumer
import java.awt.Component

abstract class LuaErrorReportSubmitter : ErrorReportSubmitter() {

    final override fun submit(
        events: Array<out IdeaLoggingEvent>?,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo>
    ): Boolean {
        return doSubmit(events, additionalInfo, parentComponent, consumer)
    }

    abstract fun doSubmit(
        events: Array<out IdeaLoggingEvent>?,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo>
    ): Boolean
}