

package com.tang.intellij.lua.psi

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ex.ApplicationManagerEx
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.fileTypes.FileTypeEvent
import com.intellij.openapi.fileTypes.FileTypeListener
import com.intellij.openapi.fileTypes.FileTypeManager
import com.tang.intellij.lua.lang.LuaFileType

class LuaFileManager : ApplicationComponent, FileTypeListener {

    private val myMessageBus = ApplicationManager.getApplication().messageBus

    private var myExtensions = mutableListOf<String>()
    private var dirty = true

    companion object {
        fun getInstance(): LuaFileManager {
            return ApplicationManagerEx.getApplicationEx().getComponent(LuaFileManager::class.java)
        }
    }

    override fun initComponent() {
        myMessageBus.connect().subscribe(FileTypeManager.TOPIC, this)
    }

    override fun disposeComponent() {
    }

    override fun getComponentName() = "LuaFileManager"

    override fun fileTypesChanged(event: FileTypeEvent) {
        dirty = true
    }

    val extensions: Array<String> get() {
        if (dirty) {
            dirty = false
            val all = FileTypeManager.getInstance().getAssociations(LuaFileType.INSTANCE).mapNotNull {
                // *.lua -> .lua
                // *.lua.txt -> .lua.txt
                if (it.presentableString.startsWith("*."))
                    it.presentableString.substring(1)
                else null
            }
            myExtensions.clear()
            myExtensions.addAll(all)
            myExtensions.add("")
        }
        return myExtensions.toTypedArray()
    }
}