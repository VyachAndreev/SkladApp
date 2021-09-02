package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.ItemShipmentHistoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class ShipmentItem(
    val number: String? = null,
    private val mPosition: Position? = null,
    private val mark: String? = null,
    private val diameter: String? = null,
    private val packing: String? = null,
    private val mass: String? = null,
) : BindableItem<ItemShipmentHistoryBinding>() {
    override fun getLayout(): Int = R.layout.item_shipment_history

    override fun bind(viewBinding: ItemShipmentHistoryBinding, position: Int) {
        if (number != null) {
            viewBinding.number = number
        } else {
            viewBinding.tvNumber.visibility = View.GONE
        }
        if (mPosition != null) {
            with(viewBinding) {
                mark = mPosition.mark
                diameter = mPosition.diameter
                packing = mPosition.packing
                mass = mPosition.mass.toString()
            }
        } else {
            viewBinding.mark = mark
            viewBinding.diameter = diameter
            viewBinding.packing = packing
            viewBinding.mass = mass
        }
    }

    override fun initializeViewBinding(view: View): ItemShipmentHistoryBinding =
        ItemShipmentHistoryBinding.bind(view)
}