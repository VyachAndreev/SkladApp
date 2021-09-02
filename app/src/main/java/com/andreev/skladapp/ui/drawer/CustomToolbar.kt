package com.andreev.skladapp.ui.drawer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ViewToolbarBinding
import com.andreev.skladapp.ui.MainActivity

class CustomToolbar(context: Context, attributeSet: AttributeSet)
    : FrameLayout(context, attributeSet) {
    val viewBinding: ViewToolbarBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_toolbar,
        this,
        true,
    )

    fun setUpDrawer(drawer: DrawerLayout, drawerView: View, mainActivity: MainActivity) {
        viewBinding.ivToolbarMenu.setOnClickListener {
            openDrawer(drawer, drawerView)
            mainActivity.hideKeyboard()
        }
    }

    fun openDrawer(drawer: DrawerLayout, drawerView: View) {
        if (!drawer.isDrawerOpen(drawerView)) drawer.openDrawer(
            GravityCompat.START
        )
    }

    fun closeDrawer(drawer: DrawerLayout, drawerView: View) {
        if (drawer.isDrawerOpen(drawerView)) drawer.closeDrawer(
            GravityCompat.START
        )
    }
}