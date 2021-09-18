

package com.tang.intellij.lua.editor.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import javax.swing.Icon

/**
 * First Created on 2016/12/28.
 */
open class LuaTreeElement(val element: NavigationItem, var name: String, val icon: Icon) : StructureViewTreeElement {
    var parent: LuaTreeElement? = null
    private val children = LinkedHashMap<String, LuaTreeElement>()

    open fun getPresentableText(): String? {
        return name
    }

    override fun getValue(): Any {
        return element
    }

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return this@LuaTreeElement.getPresentableText()
            }

            override fun getLocationString(): String? {
                return null
            }

            override fun getIcon(b: Boolean): Icon? {
                return this@LuaTreeElement.icon
            }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        return children.values.toTypedArray()
    }

    fun addChild(child: LuaTreeElement, name: String? = null) {
        children[name ?: child.name] = child
        child.parent = this
    }

    fun clearChildren() {
        children.clear()
    }

    fun childNamed(name: String): LuaTreeElement? {
        return children[name]
    }

    override fun navigate(b: Boolean) {
        element.navigate(b)
    }

    override fun canNavigate(): Boolean {
        return element.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return element.canNavigateToSource()
    }
}
