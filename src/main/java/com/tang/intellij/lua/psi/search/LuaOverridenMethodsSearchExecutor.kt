

package com.tang.intellij.lua.psi.search

import com.intellij.openapi.progress.ProgressManager
import com.intellij.util.Processor
import com.intellij.util.QueryExecutor
import com.tang.intellij.lua.psi.LuaClassMethod
import com.tang.intellij.lua.psi.guessClassType
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.stubs.index.LuaClassMemberIndex
import com.tang.intellij.lua.ty.TyClass

class LuaOverridenMethodsSearchExecutor : QueryExecutor<LuaClassMethod, LuaOverridenMethodsSearch.SearchParameters> {
    override fun execute(searchParameters: LuaOverridenMethodsSearch.SearchParameters, processor: Processor<in LuaClassMethod>): Boolean {
        val method = searchParameters.method
        val project = method.project
        val context = SearchContext.get(project)
        val type = method.guessClassType(context)
        val methodName = method.name
        if (type != null && methodName != null) {
            TyClass.processSuperClass(type, context) { superType->
                ProgressManager.checkCanceled()
                val superTypeName = superType.className
                val superMethod = LuaClassMemberIndex.findMethod(superTypeName, methodName, context)
                if (superMethod == null) true else processor.process(superMethod)
            }
        }
        return false
    }
}