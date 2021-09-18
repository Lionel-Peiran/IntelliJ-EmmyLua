

package com.tang.intellij.lua.editor.structure

import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.tang.intellij.lua.lang.LuaIcons
import com.tang.intellij.lua.psi.*

/**
 * First Created on 2016/12/13.
 */
class LuaFileElement(private val file: LuaPsiFile) : LuaTreeElement(file, file.name, LuaIcons.FILE) {
    override fun getChildren(): Array<TreeElement> {
        val visitor = LuaStructureVisitor()

        file.acceptChildren(visitor)

        visitor.compressChildren()

        return visitor.getChildren()
    }
}
