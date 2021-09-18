

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.project.DumbService
import com.intellij.util.Processor
import com.intellij.util.QueryExecutor
import com.tang.intellij.lua.comment.psi.LuaDocTagClass
import com.tang.intellij.lua.stubs.index.LuaSuperClassIndex

/**
 * LuaClassInheritorsSearchExecutor
 * First Created on 2017/3/28.
 */
class LuaClassInheritorsSearchExecutor : QueryExecutor<LuaDocTagClass, LuaClassInheritorsSearch.SearchParameters> {

    private fun processInheritors(searchParameters: LuaClassInheritorsSearch.SearchParameters,
                                  typeName: String,
                                  processedNames: MutableSet<String>,
                                  processor: Processor<in LuaDocTagClass>): Boolean {
        var ret = true
        // recursion guard!!
        if (!processedNames.add(typeName))
            return ret

        val processed = mutableListOf<LuaDocTagClass>()
        LuaSuperClassIndex.process(typeName, searchParameters.project, searchParameters.searchScope, Processor {
            processed.add(it)
            ret = processor.process(it)
            ret
        })
        if (ret && searchParameters.isDeep) {
            for (def in processed) {
                ret = processInheritors(searchParameters, def.name, processedNames, processor)
                if (!ret) break
            }
        }
        return ret
    }

    override fun execute(searchParameters: LuaClassInheritorsSearch.SearchParameters, processor: Processor<in LuaDocTagClass>): Boolean {
        var ref = true
        DumbService.getInstance(searchParameters.project).runReadActionInSmartMode {
            ref = processInheritors(searchParameters, searchParameters.typeName, mutableSetOf(), processor)
        }
        return ref
    }
}
