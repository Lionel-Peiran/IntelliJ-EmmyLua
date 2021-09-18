

package com.tang.intellij.lua.ty

import com.tang.intellij.lua.psi.LuaClassMember

class ClassMemberChain(val ty: ITyClass, var superChain: ClassMemberChain?) {
    private val members = mutableMapOf<String, LuaClassMember>()

    fun add(member: LuaClassMember) {
        val name = member.name ?: return
        val superExist = superChain?.findMember(name)
        val override = superExist == null || canOverride(member, superExist)
        if (override) {
            val selfExist = members[name]
            if (selfExist == null || member.worth > selfExist.worth)
                members[name] = member
        }
    }

    fun findMember(name: String): LuaClassMember? {
        return members.getOrElse(name) { superChain?.findMember(name) }
    }

    private fun process(deep: Boolean, processor: (ITyClass, String, LuaClassMember) -> Unit) {
        for ((t, u) in members) {
            processor(ty, t, u)
        }
        if (deep)
            superChain?.process(deep, processor)
    }

    fun process(deep: Boolean, processor: (ITyClass, LuaClassMember) -> Unit) {
        val cache = mutableSetOf<String>()
        process(deep) { clazz, name, member ->
            if (cache.add(name))
                processor(clazz, member)
        }
    }

    private fun canOverride(member: LuaClassMember, superMember: LuaClassMember): Boolean {
        return member.worth > superMember.worth || (member.worth == superMember.worth && member.worth > LuaClassMember.WORTH_ASSIGN)
    }
}