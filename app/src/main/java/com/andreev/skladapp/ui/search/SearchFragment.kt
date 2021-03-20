package com.andreev.skladapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.HintItem
import com.andreev.skladapp.ui._item.PlaqueItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class SearchFragment: BaseFragment<FragmentSearchBinding>(), Observer<String> {

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
        hintAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is HintItem -> {
                    viewModel.searchedText.value = item.hint
                    viewBinding.searchBtn.callOnClick()
                }
                else -> {}
            }
        }
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
            hideKeyBoard()
            hintAdapter.clear()
            showLoading()
            Timber.i("editText text = ${viewBinding.searchEt.text}")
            viewModel.getPositions()
        }

        viewBinding.searchEt.setOnEditorActionListener(OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewBinding.searchBtn.callOnClick()
            }
            false
        })

        viewModel.searchedText.observe(this, this)
        viewModel.hints.observe(this, hintsListener)
        viewModel.positions.observe(this, positionsListener)

        viewBinding.swipeLayout.setColorSchemeColors(resources.getColor(R.color.blue_3B4))
        viewBinding.swipeLayout.setOnRefreshListener {
            viewModel.refreshPositions()
        }
    }

    override fun onChanged(searchedText: String?) {
        viewModel.getHints(searchedText)
    }

    private val hintsListener = Observer<Array<String>> {hints ->
        hintAdapter.clear()
        hintAdapter.addAll(
            hints.map {
                HintItem(it)
            }
        )
    }

    private val positionsListener = Observer<Array<Position>> {positions ->
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
        itemsAdapter.clear()
        itemsAdapter.addAll(
           positions.map {
               PlaqueItem(it)
           }
        )
    }
}