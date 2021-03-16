package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ItemFilterBinding
import com.xwray.groupie.viewbinding.BindableItem

class FilterItem(private val name: String) : BindableItem<ItemFilterBinding>() {
    override fun getLayout(): Int = R.layout.item_filter

    override fun bind(viewBinding: ItemFilterBinding, position: Int) {
        viewBinding.name = name
    }

    override fun initializeViewBinding(view: View): ItemFilterBinding =
        ItemFilterBinding.bind(view)
}