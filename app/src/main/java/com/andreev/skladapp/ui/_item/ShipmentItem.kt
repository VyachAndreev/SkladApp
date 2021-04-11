package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.ItemShipmentHistoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class ShipmentItem(
    val number: String? = null,
    val mPosition: Position? = null,
): BindableItem<ItemShipmentHistoryBinding>() {
    override fun bind(viewBinding: ItemShipmentHistoryBinding, position: Int) {
        viewBinding.number = number
        if (mPosition != null) {
            viewBinding.mark = mPosition.mark
            viewBinding.diameter = mPosition.diameter
            viewBinding.packing = mPosition.packing
            viewBinding.mass = mPosition.mass.toString()
        } else {
            viewBinding.mark = null
            viewBinding.diameter = null
            viewBinding.packing = null
            viewBinding.mass = null
        }
    }

    override fun getLayout(): Int = R.layout.item_shipment_history

    override fun initializeViewBinding(view: View): ItemShipmentHistoryBinding =
        ItemShipmentHistoryBinding.bind(view)

}