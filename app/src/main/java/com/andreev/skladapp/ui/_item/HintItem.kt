package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.ItemSearchHintBinding
import com.xwray.groupie.viewbinding.BindableItem

class HintItem(val hint: String) : BindableItem<ItemSearchHintBinding>() {
    override fun getLayout(): Int = R.layout.item_search_hint

    override fun bind(viewBinding: ItemSearchHintBinding, position: Int) {
        viewBinding.hint = hint
    }

    override fun initializeViewBinding(view: View): ItemSearchHintBinding =
        ItemSearchHintBinding.bind(view)
}

