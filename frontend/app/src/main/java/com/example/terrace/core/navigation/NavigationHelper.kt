package com.example.terrace.core.navigation

import android.util.Log
import androidx.navigation.NavController


fun Screen.withArgs(vararg args: String): String {
    return buildString {
        append(route)
        args.forEach { append("/$it") }
    }
}

/**
 * A helper function to navigate safely.
 */
fun NavController.safeNavigate(route: String) {
    try {
        this.navigate(route)
    } catch (e: Exception) {
        Log.e("NavigationError", "Failed to navigate to $route", e)
    }
}
