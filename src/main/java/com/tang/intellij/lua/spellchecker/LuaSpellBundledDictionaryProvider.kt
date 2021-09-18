

package com.tang.intellij.lua.spellchecker

import com.intellij.spellchecker.BundledDictionaryProvider

class LuaSpellBundledDictionaryProvider : BundledDictionaryProvider {
    override fun getBundledDictionaries() = arrayOf("/spellchecker/lua.dic")
}