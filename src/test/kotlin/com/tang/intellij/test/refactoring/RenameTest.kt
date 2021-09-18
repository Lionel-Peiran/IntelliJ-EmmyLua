

package com.tang.intellij.test.refactoring

import com.tang.intellij.test.LuaTestBase

class RenameTest : LuaTestBase() {
    fun `test rename file`() = checkByDirectory("""
         --- A.lua
         print("a")
         --- B.lua
         require('A')
    """, """
         --- C.lua
         print("a")
         --- B.lua
         require('C')
    """) {
        val file = myFixture.configureFromTempProjectFile("A.lua")
        myFixture.renameElement(file, "C.lua")
    }
}