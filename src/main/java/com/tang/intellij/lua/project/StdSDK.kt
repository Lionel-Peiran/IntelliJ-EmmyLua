

package com.tang.intellij.lua.project

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl

/**
 *
 * Created by tangzx on 2017/2/6.
 */
class StdSDK : ApplicationComponent {

    override fun initComponent() {
        sdk
    }

    override fun disposeComponent() {

    }

    override fun getComponentName() = "StdSDK"

    companion object {
        private const val NAME = "Lua"

        val sdk: Sdk get() {
            val jdkTable = ProjectJdkTable.getInstance()
            //清除旧的std sdk，不用了，用predefined代替
            var value = jdkTable.findJdk(NAME)
            if (value == null) {
                value = ProjectJdkImpl(NAME, LuaSdkType.instance)
                ApplicationManager.getApplication().runWriteAction { jdkTable.addJdk(value) }
            }
            return value
        }
    }
}
