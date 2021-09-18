

package com.tang.intellij.lua.lang

import com.intellij.lang.Language
import com.intellij.openapi.util.TextRange
import com.intellij.psi.InjectedLanguagePlaces
import com.intellij.psi.LanguageInjector
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import com.tang.intellij.lua.comment.psi.LuaDocTagLan
import com.tang.intellij.lua.lang.type.LuaString
import com.tang.intellij.lua.psi.LuaCommentOwner
import com.tang.intellij.lua.psi.LuaLiteralExpr

class LuaLanguageInjector : LanguageInjector {
    override fun getLanguagesToInject(injectionHost: PsiLanguageInjectionHost, languagePlaces: InjectedLanguagePlaces) {
        if (injectionHost is LuaLiteralExpr) {
            val commentOwner = PsiTreeUtil.getParentOfType(injectionHost, LuaCommentOwner::class.java)
            if (commentOwner != null) {
                val comment = commentOwner.comment
                if (comment != null) {
                    val lanDef = PsiTreeUtil.findChildOfType(comment, LuaDocTagLan::class.java)
                    if (lanDef != null) {
                        val lan = Language.findLanguageByID(lanDef.id?.text)
                        if (lan != null) {
                            val le:LuaLiteralExpr = injectionHost
                            val content = LuaString.getContent(le.text)
                            val range = TextRange(content.start, content.end)
                            languagePlaces.addPlace(lan, range, null, null)
                        }
                    }
                }
            }
        }
    }
}