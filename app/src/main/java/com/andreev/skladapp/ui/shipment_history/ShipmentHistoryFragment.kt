package com.andreev.skladapp.ui.shipment_history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ShipmentHistoryFragment: BaseFragment<FragmentSearchBinding>() {
    lateinit var viewModel: ShipmentHistoryViewModel

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShipmentHistoryViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}