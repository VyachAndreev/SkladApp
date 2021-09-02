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
    override fun getLayout(): Int = R.layout.item_table

    override fun bind(viewBinding: ItemTableBinding, position: Int) {
        if (number != "null") {
            viewBinding.number = number
        } else {
            viewBinding.tvNumber.visibility = View.GONE
        }
        with(viewBinding) {
            if (mPosition != null) {
                mark = mPosition.mark
                diameter = mPosition.diameter
                packing = mPosition.packing
                mass0 = mPosition.mass?.get(0).toString()
                mass1 = mPosition.mass?.get(1).toString()
            } else {
                mark = this@TableItem.mark
                diameter = this@TableItem.diameter
                packing = this@TableItem.packing
                mass0 = this@TableItem.mass0
                mass1 = this@TableItem.mass1
            }
        }
    }

    override fun initializeViewBinding(view: View): ItemTableBinding = ItemTableBinding.bind(view)
}