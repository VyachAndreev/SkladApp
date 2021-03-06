package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.ItemPlaqueBinding
import com.xwray.groupie.viewbinding.BindableItem

class PlaqueItem(val pos: Position, private val isShipping: Boolean = false)
    : BindableItem<ItemPlaqueBinding>() {
    private lateinit var viewBinding: ItemPlaqueBinding

    fun getMass(): String {
        return viewBinding.weightEt.text.toString()
    }

    override fun getLayout(): Int = R.layout.item_plaque

    override fun bind(viewBinding: ItemPlaqueBinding, position: Int) {
        with(viewBinding) {
            this@PlaqueItem.viewBinding = this
            this.position = pos
            this.isShipping = this@PlaqueItem.isShipping
        }
    }

    override fun initializeViewBinding(view: View): ItemPlaqueBinding =
        ItemPlaqueBinding.bind(view)
}