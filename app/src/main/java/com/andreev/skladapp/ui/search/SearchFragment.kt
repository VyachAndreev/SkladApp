package com.andreev.skladapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.HintItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class SearchFragment: BaseFragment<FragmentSearchBinding>(), Observer<String>,
    HintItem.OnHintClickedListener {

    override fun getLayoutRes(): Int = R.layout.fragment_search

    lateinit var viewModel: SearchViewModel

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    lateinit var hintAdapter: GroupAdapter<GroupieViewHolder>
    lateinit var itemsAdapter: GroupAdapter<GroupieViewHolder>

    private fun initRecyclers() {
        initHintRecycler()
        initItemsRecycler()
    }

    private fun initHintRecycler() {
        hintAdapter = GroupAdapter()
        viewBinding.apply {
            recyclerHints.visibility = View.VISIBLE
            recyclerHints.apply {
                adapter = hintAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun initItemsRecycler() {
        itemsAdapter = GroupAdapter()
        viewBinding.apply {
            recycler.apply {
                adapter = itemsAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.viewModel = viewModel
        initRecyclers()
        viewBinding.searchBtn.setOnClickListener {
            hintAdapter.clear()
            Timber.i("editText text = ${viewBinding.searchEt.text}")
        }
        viewModel.searchedText.observe(this, this)
        viewModel.hints.observe(this, hintsListener)
    }

    override fun onChanged(searchedText: String?) {
        viewModel.getHints(searchedText)
    }

    private val hintsListener = Observer<ArrayList<String>> {hints ->
        hintAdapter.clear()
        hintAdapter.addAll(
            hints.map {
                HintItem(it)
            }
        )
    }

    override fun onHintClick(hint: String) {
        viewModel.searchedText.value = hint
        viewBinding.searchBtn.callOnClick()
    }
}