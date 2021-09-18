

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.project.DumbService
import com.intellij.util.Processor
import com.tang.intellij.lua.ty.ITyClass
import com.tang.intellij.lua.ty.createSerializedClass

/**
 *
 * First Created on 2017/3/29.
 */
class LuaAllClassesSearchExecutor : QueryExecutorBase<ITyClass, LuaAllClassesSearch.SearchParameters>() {
    override fun processQuery(searchParameters: LuaAllClassesSearch.SearchParameters, processor: Processor<in ITyClass>) {
        DumbService.getInstance(searchParameters.project).runReadActionInSmartMode {
            LuaShortNamesManager.getInstance(searchParameters.project).processAllClassNames(searchParameters.project, Processor { typeName ->
                //todo no TySerializedClass
                processor.process(createSerializedClass(typeName))
            })
        }
    }
}