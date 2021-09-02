package com.andreev.skladapp.ui.drawer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ViewDrawerItemBinding

class DrawerItem(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val viewBinding: ViewDrawerItemBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_drawer_item,
        this,
        true,
    )

    init {
        attrs?.let {
            val arr = context.obtainStyledAttributes(
                it,
                R.styleable.DrawerItem,
            )
            with(viewBinding) {
                title = arr.getString(R.styleable.DrawerItem_d_title)
                dividerBottom = arr.getBoolean(
                    R.styleable.DrawerItem_d_dividerBottom,
                    true,
                )
            }
            arr.recycle()
        }
    }
}