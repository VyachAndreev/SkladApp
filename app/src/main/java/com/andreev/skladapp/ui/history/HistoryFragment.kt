package com.andreev.skladapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.ShipmentItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class HistoryFragment : BaseFragment<FragmentSearchBinding>() {
    lateinit var viewModel: HistoryViewModel
    override fun getLayoutRes(): Int = R.layout.fragment_search
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setHasStableIds(true)
        viewBinding.linear.visibility = View.GONE
        viewBinding.recycler.apply {
            adapter = this@HistoryFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.historyPiecesData.observe(this, historyObserver)
        showLoading()
        viewModel.getHistory()
        viewBinding.swipeLayout.setColorSchemeColors(resources.getColor(R.color.blue_3B4))
        viewBinding.swipeLayout.setOnRefreshListener {
            viewModel.getHistory()
        }
    }

    val historyObserver = Observer<Array<HistoryPiece>> {
        adapter.clear()
        adapter.add(
            ShipmentItem(
                number = "Номер отгрузки",
                mark = "Номер счета",
                diameter = "Агент",
                packing = "Дата",
                mass = "Масса",
                )
        )
        adapter.addAll(
            it.map { piece ->
                ShipmentItem(
                    number = piece.id.toString(),
                    mark = piece.bill,
                    diameter = piece.contrAgent,
                    packing = piece.date,
                    mass = piece.mass,
                )
            }
        )
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
    }
}