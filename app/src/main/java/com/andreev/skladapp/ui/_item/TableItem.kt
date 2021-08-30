package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.MockPosition
import com.andreev.skladapp.databinding.ItemTableBinding
import com.xwray.groupie.viewbinding.BindableItem

class TableItem(
    val number: String? = null,
    val mPosition: MockPosition? = null,
    val mark: String? = null,
    val diameter: String? = null,
    val packing: String? = null,
    val mass0: String? = null,
    val mass1: String? = null,
): BindableItem<ItemTableBinding>() {
    override fun bind(viewBinding: ItemTableBinding, position: Int) {
        if (number != "null") {
            viewBinding.number = number
        } else {
            viewBinding.tvNumber.visibility = View.GONE
        }
        if (mPosition != null) {
            viewBinding.mark = mPosition.mark
            viewBinding.diameter = mPosition.diameter
            viewBinding.packing = mPosition.packing
            viewBinding.mass0 = mPosition.mass?.get(0).toString()
            viewBinding.mass1 = mPosition.mass?.get(1).toString()
        } else {
            viewBinding.mark = mark
            viewBinding.diameter = diameter
            viewBinding.packing = packing
            viewBinding.mass0 = mass0
            viewBinding.mass1 = mass1
        }
    }

    override fun getLayout(): Int = R.layout.item_table

    override fun initializeViewBinding(view: View): ItemTableBinding =
        ItemTableBinding.bind(view)

}