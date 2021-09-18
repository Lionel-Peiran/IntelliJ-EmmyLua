

package com.tang.intellij.lua.ext

import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.RecursionManager

fun <T> recursionGuard(key: Any, block: Computable<T>, memoize: Boolean = true): T? =
        RecursionManager.doPreventingRecursion(key, memoize, block)