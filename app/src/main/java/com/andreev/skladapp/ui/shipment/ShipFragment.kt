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
import com.andreev.skladapp.ui._base.BaseChildFragment
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.utils.Extensions.isVisible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class ShipFragment : BaseChildFragment<FragmentShipmentBinding>() {
    private lateinit var viewModel: ShipViewModel
    private val adapter by lazy { GroupAdapter<GroupieViewHolder>() }
    var isPrevLayout = true

    override fun getLayoutRes(): Int = R.layout.fragment_shipment

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShipViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding.recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ShipFragment.adapter
        }
    }

    private fun clearET() {
        with(viewBinding) {
            exceptEt.text.clear()
            shipEt.text.clear()
        }
    }

    fun setPostLayout() {
        isPrevLayout = false
        with(viewBinding) {
            tvUpper.setText(R.string.ship_contr_agent)
            tvLower.setText(R.string.ship_bill)
            recycler.visibility = visible
        }
        clearET()
    }

    fun setPrevLayout() {
        isPrevLayout = true
        with(viewBinding) {
            tvUpper.setText(R.string.ship_ship_semi)
            tvLower.setText(R.string.ship_except_semi)
            recycler.visibility = gone
        }
        clearET()
    }

    override fun setListeners() {
        with(viewBinding) {
            scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                Timber.i("scroll changed")
                if (!isPrevLayout) {
                    Timber.i("post")
                    if (btnShip.isVisible(relative)) {
                        Timber.i("visible")
                        fbtn.visibility = gone
                    } else {
                        Timber.i("not visible")
                        fbtn.visibility = visible
                    }
                }
            }
            fbtn.setOnClickListener {
                scrollView.fullScroll(View.FOCUS_UP)
            }
            btnShip.setOnClickListener {
                hideKeyBoard()
                showLoading()
                if (isPrevLayout) {
                    viewModel.departure(
                        shipEt.text.toString(),
                        exceptEt.text.toString(),
                    )
                } else {
                    val arrayList: ArrayList<Pair<Long, String>> = arrayListOf()
                    for (i in 0 until viewModel.positions.value?.size!!) {
                        with(adapter.getItem(i) as PlaqueItem) {
                            Timber.i("id: ${pos.id}, mass: ${getMass()}")
                            arrayList.add(pos.id!! to getMass())
                        }
                    }
                    viewModel.confirm(
                        arrayList.toTypedArray(),
                        shipEt.text.toString(),
                        exceptEt.text.toString(),
                    )
                }
            }
        }
    }

    override fun setObservers() {with(viewModel) {
            positions.observe(this@ShipFragment, positionsObserver)
            confirmResponse.observe(this@ShipFragment, confirmObserver)
            toastText.observe(this@ShipFragment, toastObserver)
        }
    }

    private val confirmObserver =
        Observer<ItemsRepository.ConfirmResponse> {
            hideLoading()
            setPrevLayout()
        }

    private val positionsObserver = Observer<Array<Position>> { positions ->
        with(adapter) {
            clear()
            addAll(positions.map { PlaqueItem(it, true) })
        }
        hideLoading()
        if (isPrevLayout) {
            setPostLayout()
        }
    }

    private val toastObserver = Observer<String> {
        showToast(it)
    }
}