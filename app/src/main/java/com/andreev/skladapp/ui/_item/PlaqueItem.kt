package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.ItemPlaqueBinding
import com.xwray.groupie.viewbinding.BindableItem

class PlaqueItem(val pos: Position) : BindableItem<ItemPlaqueBinding>() {

    override fun getLayout(): Int = R.layout.item_plaque

    override fun bind(viewBinding: ItemPlaqueBinding, position: Int) {
        viewBinding.position = pos
    }

    override fun initializeViewBinding(view: View): ItemPlaqueBinding =
        ItemPlaqueBinding.bind(view)
}