package com.andreev.skladapp.ui.shipment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.databinding.FragmentShipmentBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment
import kotlinx.android.synthetic.main.fragment_information.*
import kotlinx.android.synthetic.main.fragment_shipment.*

class ShipmentFragment: BaseFragment<FragmentShipmentBinding>() {
    lateinit var viewModel: ShipmentViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_shipment

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShipmentViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
        viewBinding.btnShip.setOnClickListener {
                viewModel.departure(
                    viewBinding.shipEt.text.toString(),
                    viewBinding.exceptEt.text.toString(),
                )
            }
    }
}