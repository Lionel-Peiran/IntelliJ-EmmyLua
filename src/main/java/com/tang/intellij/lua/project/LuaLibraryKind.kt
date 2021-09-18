

package com.tang.intellij.lua.project

import com.intellij.openapi.roots.libraries.DummyLibraryProperties
import com.intellij.openapi.roots.libraries.PersistentLibraryKind

/**
 *
 * First Created on 2016/12/24.
 */
class LuaLibraryKind private constructor() : PersistentLibraryKind<DummyLibraryProperties>("Lua") {

    override fun createDefaultProperties() = DummyLibraryProperties()

    companion object {
        var INSTANCE = LuaLibraryKind()
    }
}
