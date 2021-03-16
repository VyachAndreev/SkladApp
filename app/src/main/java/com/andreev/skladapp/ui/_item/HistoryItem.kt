package com.andreev.skladapp.ui._item

import android.view.View
import com.andreev.skladapp.R
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.databinding.ItemHistoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class HistoryItem(private val historyPiece: HistoryPiece) : BindableItem<ItemHistoryBinding>() {

    override fun getLayout(): Int = R.layout.item_history

    override fun bind(viewBinding: ItemHistoryBinding, position: Int) {
        viewBinding.historyPiece = historyPiece
    }

    override fun initializeViewBinding(view: View): ItemHistoryBinding =
        ItemHistoryBinding.bind(view)

}