package com.andreev.skladapp.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import java.lang.ref.WeakReference

object ResourceProvider {
    private lateinit var context: WeakReference<Context>

    fun setContext(context: Context) {
        this.context = WeakReference(context)
    }

    fun getString(@StringRes stringRes: Int): String? {
        return context.get()?.getString(stringRes)
    }

    fun clearContext() {
        context.clear()
    }
}