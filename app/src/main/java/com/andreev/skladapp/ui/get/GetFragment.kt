package com.andreev.skladapp.ui.get

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseChildFragment
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment

class GetFragment : BaseChildFragment<FragmentSearchBinding>() {
    private lateinit var viewModel: GetViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(GetViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setListeners() {
        with(viewBinding.searchBtn) {
            setImageResource(R.drawable.ic_arrow_right)
            setOnClickListener {
                viewModel.get(viewBinding.searchEt.text.toString())
            }
        }
    }

    override fun setObservers() {
        // No observers
    }
}