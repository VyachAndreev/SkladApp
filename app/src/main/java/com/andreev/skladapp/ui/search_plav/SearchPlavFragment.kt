package com.andreev.skladapp.ui.search_plav

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.ShipmentItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.search.SearchFragment

class SearchPlavFragment: SearchFragment() {

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchPlavViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override val positionsListener = Observer<Array<Position>> { positions ->
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
        itemsAdapter.clear()
        if (positions.isNotEmpty()) {
            itemsAdapter.add(
                ShipmentItem()
            )
            var i = 0
            for (position in positions) {
                i++
                itemsAdapter.add(
                    ShipmentItem(
                        i.toString(),
                        position,
                    )
                )
            }
        } else {
            itemsAdapter.add(TextViewItem("Ничего не найдено"))
        }
    }
}