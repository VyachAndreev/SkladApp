package com.andreev.skladapp.ui.utils

import android.graphics.Rect
import android.view.View
import android.widget.RelativeLayout

object Extensions {
    fun View?.isVisible(layout: RelativeLayout): Boolean {
        if (this == null) {
            return false
        }
        if (!this.isShown) {
            return false
        }
        val actualPosition = Rect()
        this.getGlobalVisibleRect(actualPosition)
        val screen = Rect(0, 0, layout.width, layout.height)
        return actualPosition.intersect(screen)
    }
}