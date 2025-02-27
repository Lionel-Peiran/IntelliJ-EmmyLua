

package com.tang.intellij.lua.project

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.impl.ProjectFileIndexFacade
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.util.messages.Topic

interface LuaSourceRootListener {
    fun onChanged()
}

@State(name = "LuaSourceRootManager", storages = [(Storage("emmy.xml"))])
class LuaSourceRootManager(val project: Project) : PersistentStateComponent<LuaSourceRootManager.State> {
    companion object {
        val TOPIC: Topic<LuaSourceRootListener> = Topic.create("lua project source root changes", LuaSourceRootListener::class.java)

        fun getInstance(project: Project): LuaSourceRootManager {
            return project.getComponent(LuaSourceRootManager::class.java)
        }
    }

    private var state = State()

    fun appendRoot(dir: VirtualFile) {
        state.rootList.add(dir.url)
        project.messageBus.syncPublisher(TOPIC).onChanged()
        project.save()
    }

    fun removeRoot(dir: VirtualFile) {
        state.rootList.removeAll { it == dir.url }
        project.messageBus.syncPublisher(TOPIC).onChanged()
        project.save()
    }

    fun getSourceRootUrls(): List<String> {
        val list = mutableListOf<String>()
        list.addAll(state.rootList)

        val modules = ModuleManager.getInstance(project).modules
        for (module in modules) {
            val moduleRootManager = ModuleRootManager.getInstance(module)
            val sourceRoots = moduleRootManager.sourceRootUrls
            list.addAll(sourceRoots)
            //content root
            //val contentRoots = moduleRootManager.contentRootUrls
            //list.addAll(contentRoots)
        }
        return list
    }

    fun getSourceRoots(): List<VirtualFile> {
        val list = mutableListOf<VirtualFile>()
        val modules = ModuleManager.getInstance(project).modules
        for (module in modules) {
            val moduleRootManager = ModuleRootManager.getInstance(module)
            list.addAll(moduleRootManager.sourceRoots)
            //list.addAll(moduleRootManager.contentRoots)
        }
        for (url in state.rootList) {
            val file = VirtualFileManager.getInstance().findFileByUrl(url)
            if (file != null)
                list.add(file)
        }
        return list
    }

    fun isSourceRoot(dir: VirtualFile): Boolean {
        return state.rootList.contains(dir.url)
    }

    fun isInSource(file: VirtualFile): Boolean {
        if (ProjectFileIndexFacade.getInstance(project).isInSource(file))
            return true
        val url = file.url
        for (rootUrl in state.rootList) {
            if (url.startsWith(rootUrl))
                return true
        }
        return false
    }

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

    class State {
        var rootList: MutableList<String> = mutableListOf()
    }
}