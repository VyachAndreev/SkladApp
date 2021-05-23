package com.andreev.skladapp.ui.shipment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentShipmentBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.network.repositories.ItemsRepository
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ShipmentFragment : BaseFragment<FragmentShipmentBinding>() {
    lateinit var viewModel: ShipmentViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_shipment

    val adapter = GroupAdapter<GroupieViewHolder>()

    var isPred = true

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShipmentViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (parentFragment as HubFragment).viewModel.curMenuItem.value = this

        with(viewBinding.recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }

        viewModel.positions.observe(this, positionsObserver)
        viewModel.confirmResponse.observe(this, confirmObserver)
        viewModel.toastText.observe(this, toastObserver)
        viewBinding.btnShip.setOnClickListener {
            showLoading()
            if (isPred) {
                viewModel.departure(
                    viewBinding.shipEt.text.toString(),
                    viewBinding.exceptEt.text.toString(),
                )
            } else {
                val array: Array<Pair<Long?, String?>> = arrayOf()
                for (i in 0..viewModel.positions.value?.size!!) {
                    with(adapter.getItem(i) as PlaqueItem) {
                        array.plusElement(this.pos.id to this.getMass())
                    }
                }
                viewModel.confirm(
                    array,
                    viewBinding.shipEt.text.toString(),
                    viewBinding.exceptEt.text.toString(),
                )
            }
        }
    }

    private val confirmObserver =
        Observer<ItemsRepository.ConfirmResponse> {
            hideLoading()
            setPredLayout()
        }

    private val positionsObserver = Observer<Array<Position>> { positions ->
        adapter.clear()
        adapter.addAll(
            positions.map {
                PlaqueItem(it, it.mass)
            }
        )
        hideLoading()
        if (isPred) {
            setPostLayout()
        }
    }

    private val toastObserver = Observer<String> {
        showToast(it)
    }

    fun setPostLayout() {
        isPred = false
        with(viewBinding) {
            tvUpper.setText(R.string.contr_agent)
            tvLower.setText(R.string.bill)
            recycler.visibility = View.VISIBLE
        }
        clearET()
    }

    fun setPredLayout() {
        isPred = true
        with(viewBinding) {
            tvUpper.setText(R.string.ship_semi)
            tvLower.setText(R.string.except_semi)
            recycler.visibility = View.GONE
        }
        clearET()
    }

    private fun clearET() {
        viewBinding.exceptEt.text.clear()
        viewBinding.shipEt.text.clear()
    }
}