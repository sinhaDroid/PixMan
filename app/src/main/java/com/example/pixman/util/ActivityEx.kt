package com.example.pixman.util

import androidx.fragment.app.FragmentManager

inline fun <reified T> getFragmentInstance(fragmentManager: FragmentManager,
                                           fragID: Int, block: T.() -> Unit) {
    val navHostFragment = fragmentManager.findFragmentById(fragID)
    navHostFragment?.childFragmentManager?.fragments?.forEach { fragment ->
        if (fragment.javaClass.simpleName == T::class.java.simpleName && fragment.isVisible) {
            block(fragment as T)
        }
    }
}