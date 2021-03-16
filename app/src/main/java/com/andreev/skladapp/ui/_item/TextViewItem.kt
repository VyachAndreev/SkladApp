package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ItemTextViewBinding
import com.xwray.groupie.viewbinding.BindableItem

class TextViewItem(private val text: String) : BindableItem<ItemTextViewBinding>() {
    override fun getLayout(): Int = R.layout.item_text_view

    override fun bind(viewBinding: ItemTextViewBinding, position: Int) {
        viewBinding.text = text
    }

    override fun initializeViewBinding(view: View): ItemTextViewBinding =
        ItemTextViewBinding.bind(view)

}