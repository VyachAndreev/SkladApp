package com.andreev.skladapp.ui.unite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseChildFragment
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment

class UniteFragment: BaseChildFragment<FragmentSearchBinding>() {
    private lateinit var viewModel: UniteViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(UniteViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.searchBtn.setImageResource(R.drawable.ic_arrow_right)
    }

    override fun setListeners() {
        viewBinding.searchBtn.setOnClickListener {
            viewModel.unite(viewBinding.searchEt.text.toString())
        }
    }

    override fun setObservers() {
        // No observers
    }
}