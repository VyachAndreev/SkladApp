package com.andreev.skladapp.ui.search_plav

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.Constants
import com.andreev.skladapp.R
import com.andreev.skladapp.data.MockPosition
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.databinding.FragmentSearchPlavBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.*
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.information.InformationFragment
import com.andreev.skladapp.ui.search.SearchFragment
import com.andreev.skladapp.ui.search.SearchViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class SearchPlavFragment: BaseFragment<FragmentSearchPlavBinding>() {
    private lateinit var viewModel: SearchPlavViewModel
    private val hintAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val itemsAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private var isKeyBoardVisible = true

    override fun getLayoutRes(): Int = R.layout.fragment_search_plav

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchPlavViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
        with(viewBinding) {
            root.setOnClickListener {
                recyclerHints.visibility = gone
            }
            hideLoading()
            viewModel = this@SearchPlavFragment.viewModel

            initHintRecycler()
            initItemsRecycler()
        }

        setListeners()

        with(viewModel) {
            searchedText.observe(this@SearchPlavFragment, searchObserver)
            hints.observe(this@SearchPlavFragment, hintsObserver)
            positions.observe(this@SearchPlavFragment, positionsObserver)
        }
    }

    private fun initHintRecycler() {
        hintAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is HintItem -> {
                    hideKeyBoard()
                    viewModel.searchedText.value = item.hint
                    viewBinding.searchBtn.callOnClick()
                    isKeyBoardVisible = false
                    hideKeyBoard()
                }
                else -> {
                }
            }
        }
        viewBinding.apply {
            with(recyclerHints) {
                visibility = visible
                apply {
                    adapter = hintAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    private fun initItemsRecycler() {
        itemsAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PlaqueItem -> {
                    val args = Bundle()
                    item.pos.id?.let { args.putLong(Constants.ID, it) }
                    Timber.i(item.pos.type)
                    args.putBoolean(
                        Constants.IS_PACKAGE,
                        item.pos.type == getString(R.string.pallet)
                    )
                    (parentFragment as HubFragment).apply {
                        launchChildFragment(
                            InformationFragment(),
                            true,
                            args,
                        )
                    }
                }
                else -> {
                }
            }
        }
        with(viewBinding.recycler) {
            adapter = itemsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setListeners() {
        with(viewBinding) {
            searchBtn.setOnClickListener {
                hideKeyBoard()
                hintAdapter.clear()
                showLoading()
                Timber.i("editText text: ${searchEt.text}")
                this@SearchPlavFragment.viewModel.getPositions()
            }
            searchEt.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBtn.callOnClick()
                }
                false
            }

            searchEt.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.length - this@SearchPlavFragment.viewModel.searchedSize > 1) {
                        searchBtn.callOnClick()
                    }
                    this@SearchPlavFragment.viewModel.searchedSize = it.length
                }
            }

            searchEt.setOnClickListener {
                isKeyBoardVisible = true
            }

            with(swipeLayout) {
                setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.blue_3B4)
                )
                setOnRefreshListener {
                    this@SearchPlavFragment.viewModel.refreshPositions()
                }
            }
        }
    }

    private val searchObserver = Observer<String> {
        viewModel.getHints(it)
    }

    private val hintsObserver = Observer<Array<String>> { hints ->
        viewBinding.recyclerHints.visibility = if (isKeyBoardVisible) {
            visible
        } else {
            gone
        }
        with(hintAdapter) {
            clear()
            addAll(hints.map { HintItem(it) })
        }
    }

    private val positionsObserver = Observer<Array<MockPosition>> { positions ->
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
        itemsAdapter.clear()
        if (positions.isNotEmpty()) {
            itemsAdapter.add(
                TableItem(
                    number = getString(R.string.search_plav_number),
                    mark = getString(R.string.search_plav_mark),
                    diameter = getString(R.string.search_plav_diameter),
                    packing = getString(R.string.search_plav_packing),
                    mass0 = getString(R.string.search_plav_mass0),
                    mass1 = getString(R.string.search_plav_mass1),
                )
            )
            positions.forEachIndexed { index, position ->
                itemsAdapter.add(TableItem((index + 1).toString(), position))
            }
        } else {
            itemsAdapter.add(TextViewItem(getString(R.string.item_nothing_found)))
        }
    }
}