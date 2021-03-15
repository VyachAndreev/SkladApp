package com.andreev.skladapp.ui.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._adapters.HintAdapter
import com.andreev.skladapp.ui._base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import timber.log.Timber

class SearchFragment: BaseFragment<FragmentSearchBinding>(), Observer<String>,
HintAdapter.ClickListener{
    lateinit var viewModel: SearchViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.viewModel = viewModel
        viewBinding.searchBtn.setOnClickListener {
            Timber.i("editText text = ${viewBinding.searchEt.text}")
        }
        viewModel.searchedText.observe(this, this)
        viewModel.hints.observe(this, hintsObserver)
    }

    override fun onChanged(searchedText: String?) {
        viewModel.getHints(searchedText)
    }

    private val hintsObserver = Observer<ArrayList<String>> {
        HintAdapter(context!!, it, this).also{ adapter ->
            viewBinding.searchEt.setAdapter(adapter)
        }
    }

    override fun onClick() {
        viewBinding.searchBtn.callOnClick()
    }
}