package com.andreev.skladapp.ui._item

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.ItemPlaqueBinding
import com.xwray.groupie.viewbinding.BindableItem

class PlaqueItem(val pos: Position, private val mass: String? = null)
    : BindableItem<ItemPlaqueBinding>() {

    lateinit var viewBinding: ItemPlaqueBinding

    override fun getLayout(): Int = R.layout.item_plaque

    override fun bind(viewBinding: ItemPlaqueBinding, position: Int) {
        with(viewBinding) {
            this@PlaqueItem.viewBinding = this
            this.position = pos
            this.mass = this@PlaqueItem.mass
        }
    }

    override fun initializeViewBinding(view: View): ItemPlaqueBinding =
        ItemPlaqueBinding.bind(view)

    fun getMass(): String? {
        return viewBinding.mass
    }
}