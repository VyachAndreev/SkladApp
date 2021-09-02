package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.MockPosition
import com.andreev.skladapp.databinding.ItemTableBinding
import com.xwray.groupie.viewbinding.BindableItem

class TableItem(
    private val number: String? = null,
    private val mPosition: MockPosition? = null,
    private val mark: String? = null,
    private val diameter: String? = null,
    private val packing: String? = null,
    private val mass0: String? = null,
    private val mass1: String? = null,
) : BindableItem<ItemTableBinding>() {
    override fun getLayout(): Int = R.layout.item_table

    override fun bind(viewBinding: ItemTableBinding, position: Int) {
        with(viewBinding) {
            if (this@TableItem.number != null) {
                number = this@TableItem.number
            } else {
                tvNumber.visibility = View.GONE
            }
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