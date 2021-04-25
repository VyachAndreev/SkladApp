package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.TablePiece
import com.andreev.skladapp.databinding.ItemTableBinding
import com.xwray.groupie.viewbinding.BindableItem

class TableItem(private val tablePiece: TablePiece) : BindableItem<ItemTableBinding>() {

    override fun getLayout(): Int = R.layout.item_table

    override fun bind(viewBinding: ItemTableBinding, position: Int) {
        viewBinding.tablePiece = tablePiece
    }

    override fun initializeViewBinding(view: View): ItemTableBinding =
        ItemTableBinding.bind(view)

}