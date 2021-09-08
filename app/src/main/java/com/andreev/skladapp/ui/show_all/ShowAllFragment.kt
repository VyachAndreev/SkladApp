package com.andreev.skladapp.ui.show_all

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.Constants
import com.andreev.skladapp.R
import com.andreev.skladapp.data.MockPosition
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentShowAllBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._adapter.SelectionAdapter
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.TableItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.information.InformationFragment
import com.andreev.skladapp.ui.utils.DialogUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_information.*
import timber.log.Timber

class ShowAllFragment : BaseFragment<FragmentShowAllBinding>() {
    private val adapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val spinnerAdapter by lazy {
        context?.let {
            SelectionAdapter(
                it,
                R.layout.item_text_view,
                resources.getStringArray(R.array.show_all_showing_type).toList(),
            )
        }
    }
    private val position by lazy { getString(R.string.show_all_position) }
    private lateinit var viewModel: ShowAllViewModel
    private var isTable = false

    override fun getLayoutRes(): Int = R.layout.fragment_show_all

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShowAllViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (parentFragment as HubFragment).viewModel.curMenuItem.value = this

        initRecycler()
        showLoading()
        viewModel.apply {
            positions.observe(this@ShowAllFragment, positionObserver)
            tablePositions.observe(this@ShowAllFragment, tablePositionObserver)
            getPositions()
            getMarks()
            getDiameter()
            getPackings()
        }

        setListeners()
    }

    private inner class SpinnerInteractionListener : AdapterView.OnItemSelectedListener,
        View.OnTouchListener {
        var userSelect = false
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            userSelect = true
            v.performClick()
            return false
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //not used
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            pos: Int,
            id: Long
        ) {
            if (userSelect) {
                userSelect = false
                spinnerAdapter?.selectedItem = pos
                adapter.clear()
                showLoading()
                with(viewModel) {
                    if (pos == 0) {
                        isTable = false
                        tablePositions.value = arrayOf()
                        if (filterData != null) {
                            filter(filterData!!)
                        } else {
                            getPositions()
                        }
                    } else {
                        isTable = true
                        positions.value = arrayOf()
                        if (filterData != null) {
                            filterTable(filterData!!)
                        } else {
                            getTable()
                        }
                    }
                }
                spinnerAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setListeners() {
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PlaqueItem -> {
                    val args = Bundle()
                    item.pos.id?.let { args.putLong(Constants.ID, it) }
                    Timber.i(item.pos.type)
                    args.putBoolean(Constants.IS_PACKAGE, item.pos.type == position)
                    (parentFragment as HubFragment).apply {
                        launchChildFragment(
                            InformationFragment(),
                            true,
                            args,
                        )
                    }
                }
                else -> {}
            }
        }

        with(viewBinding) {
            openFilter.setOnClickListener {
                with(this@ShowAllFragment.viewModel) {
                    DialogUtils.showFilterDialog(
                        context,
                        marks.value,
                        diameter.value,
                        packings.value,
                    ) {
                        showLoading()
                        if (isTable) {
                            filterTable(it)
                        } else {
                            filter(it)
                        }
                    }
                }
            }

            val listener = SpinnerInteractionListener()
            spinner.apply {
                setOnTouchListener(listener)
                onItemSelectedListener = listener
            }

            setSwipeLayoutListener(swipeLayout) {
                with(this@ShowAllFragment.viewModel) {
                    if (filterData != null) {
                        if (isTable) {
                            filterTable(filterData!!)
                        } else {
                            filter(filterData!!)
                        }
                    } else {
                        if (isTable) {
                            getTable()
                        } else {
                            getPositions()
                        }
                    }
                }
            }
        }
    }


    private val positionObserver = Observer<Array<Position>> { positions ->
        if (!isTable) {
            hideLoading()
            adapter.clear()
            if (positions.isNotEmpty()) {
                adapter.addAll(
                    positions.map { position ->
                        PlaqueItem(position)
                    }
                )
            } else {
                adapter.add(TextViewItem(getString(R.string.item_nothing_found)))
            }
        }
    }

    private val tablePositionObserver = Observer<Array<MockPosition>> { positions ->
        if (isTable) {
            hideLoading()
            with(adapter) {
                clear()
                if (positions.isNotEmpty()) {
                    add(TableItem(number = null))
                    addAll(positions.map { TableItem(null, it) })
                } else {
                    add(TextViewItem(getString(R.string.item_nothing_found)))
                }
            }
        }
    }

    private fun initRecycler() {
        with(viewBinding) {
            spinner.adapter = spinnerAdapter
            with(recycler) {
                adapter = this@ShowAllFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}