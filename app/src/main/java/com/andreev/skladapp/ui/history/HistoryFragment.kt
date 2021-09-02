package com.andreev.skladapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.ShipmentItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class HistoryFragment : BaseFragment<FragmentSearchBinding>() {
    private lateinit var viewModel: HistoryViewModel

    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
        adapter.setHasStableIds(true)
        with(viewBinding) {
            linear.visibility = View.GONE
            recycler.apply {
                adapter = this@HistoryFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }
            with(swipeLayout) {
                setColorSchemeColors(ContextCompat.getColor(context, R.color.blue_3B4))
                setOnRefreshListener {
                    this@HistoryFragment.viewModel.getHistory()
                }
            }
        }
        viewModel.historyPiecesData.observe(this, historyObserver)
        showLoading()
        viewModel.getHistory()
    }

    private val historyObserver = Observer<Array<HistoryPiece>> { pieces ->
        with (adapter) {
            clear()
            add(
                ShipmentItem(
                    number = getString(R.string.history_shipment_number),
                    mark = getString(R.string.history_bill_number),
                    diameter = getString(R.string.history_agent),
                    packing = getString(R.string.history_date),
                    mass = getString(R.string.history_mass),
                )
            )
            pieces.reverse()
            addAll(
                pieces.map { piece ->
                    ShipmentItem(
                        number = piece.id.toString(),
                        mark = piece.bill,
                        diameter = piece.contrAgent,
                        packing = piece.date,
                        mass = piece.mass,
                    )
                }
            )
        }
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
    }
}