package com.andreev.skladapp.ui.drawer

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ViewToolbarBinding

class CustomToolbar(context: Context, attributeSet: AttributeSet)
    : FrameLayout(context, attributeSet) {
    val viewBinding: ViewToolbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_toolbar,
        this,
        true
    )

    fun setUpDrawer(drawer: DrawerLayout, drawerView: View) {
        viewBinding.ivToolbarMenu.setOnClickListener {
            openDrawer(drawer, drawerView)
        }
    }

    fun openDrawer(drawer: DrawerLayout, drawerView: View) {
        if (!drawer.isDrawerOpen(drawerView)) drawer.openDrawer(
            Gravity.LEFT
        )
    }
}