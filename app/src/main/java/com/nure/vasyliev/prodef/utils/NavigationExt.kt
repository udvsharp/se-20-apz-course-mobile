package com.nure.vasyliev.prodef.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun <T> Fragment.getNavResult(@IdRes destination: Int, key: String) =
    findNavController().getBackStackEntry(destination).savedStateHandle.get<T>(key)

fun <T> Fragment.setNavResult(@IdRes destination: Int, key: String, result: T) {
    findNavController().getBackStackEntry(destination).savedStateHandle[key] = result
}
