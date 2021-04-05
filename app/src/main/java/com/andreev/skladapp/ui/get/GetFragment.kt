package com.andreev.skladapp.ui.get

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment

class GetFragment: BaseFragment<FragmentSearchBinding>() {
    lateinit var viewModel: GetViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(GetViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.searchBtn.setImageResource(R.drawable.ic_arrow_right)
        viewBinding.searchBtn.setOnClickListener {
            viewModel.get(viewBinding.searchEt.text.toString())
        }
    }
}