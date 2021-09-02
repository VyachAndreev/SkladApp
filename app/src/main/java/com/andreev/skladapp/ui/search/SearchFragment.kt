package com.andreev.skladapp.ui.search

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
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentSearchBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.HintItem
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.information.InformationFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

open class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    protected lateinit var viewModel: SearchViewModel
    private val hintAdapter: GroupAdapter<GroupieViewHolder> by lazy { GroupAdapter() }
    protected val itemsAdapter: GroupAdapter<GroupieViewHolder> by lazy { GroupAdapter() }
    private var isKeyBoardVisible = true

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
        with(viewBinding) {
            root.setOnClickListener {
                recyclerHints.visibility = View.GONE
            }
            hideLoading()
            viewModel = this@SearchFragment.viewModel

            initHintRecycler()
            initItemsRecycler()
        }

        setListeners()

        with(viewModel) {
            searchedText.observe(this@SearchFragment, searchObserver)
            hints.observe(this@SearchFragment, hintsObserver)
            positions.observe(this@SearchFragment, positionsObserver)
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
                    args.putBoolean(Constants.IS_PACKAGE, item.pos.type == "PACKAGE")
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
                this@SearchFragment.viewModel.getPositions()
            }
            searchEt.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBtn.callOnClick()
                }
                false
            }

            searchEt.doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.length - this@SearchFragment.viewModel.searchedSize > 1) {
                        searchBtn.callOnClick()
                    }
                    this@SearchFragment.viewModel.searchedSize = it.length
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
                    this@SearchFragment.viewModel.refreshPositions()
                }
            }
        }
    }

    private val searchObserver = Observer<String> {
        viewModel.getHints(it)
    }

    private val hintsObserver = Observer<Array<String>> { hints ->
        if (isKeyBoardVisible) {
            viewBinding.recyclerHints.visibility = View.VISIBLE
        } else {
            viewBinding.recyclerHints.visibility = View.GONE
        }
        hintAdapter.clear()
        hintAdapter.addAll(
            hints.map {
                HintItem(it)
            }
        )
    }

    protected open val positionsObserver = Observer<Array<Position>> { positions ->
        hideLoading()
        viewBinding.swipeLayout.isRefreshing = false
        itemsAdapter.clear()
        if (positions.isNotEmpty()) {
            itemsAdapter.addAll(
                positions.map {
                    PlaqueItem(it)
                }
            )
        } else {
            itemsAdapter.add(TextViewItem("Ничего не найдено"))
        }
    }
}