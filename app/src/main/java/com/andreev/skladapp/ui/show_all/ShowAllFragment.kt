package com.andreev.skladapp.ui.show_all

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
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

    private var adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var spinnerAdapter: SelectionAdapter<String>
    lateinit var viewModel: ShowAllViewModel
    private var isTable = false

    override fun getLayoutRes(): Int = R.layout.fragment_show_all

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShowAllViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
        adapter.setOnItemClickListener { item, _ ->
            when (item) {
                is PlaqueItem -> {
                    val args = Bundle()
                    item.pos.id?.let { args.putLong(Constants.ID, it) }
                    Timber.i(item.pos.type)
                    args.putBoolean(Constants.IS_PACKAGE, item.pos.type == "POSITION")
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

        initRecycler()
        showLoading()
        viewModel.positions.observe(this, positionObserver)
        viewModel.tablePositions.observe(this, tablePositionObserver)
        viewModel.apply {
            getPositions()
            getMarks()
            getDiameter()
            getPackings()
        }
        viewBinding.openFilter.setOnClickListener {
            DialogUtils.showFilterDialog(
                context,
                viewModel.marks.value,
                viewModel.diameter.value,
                viewModel.packings.value,
            ) {
                showLoading()
                if (isTable) {
                    viewModel.filterTable(it)
                } else {
                    viewModel.filter(it)
                }
            }
        }

        val listener = SpinnerInteractionListener()
        viewBinding.spinner.apply {
            setOnTouchListener(listener)
            onItemSelectedListener = listener
        }

        viewBinding.swipeLayout.setColorSchemeColors(resources.getColor(R.color.blue_3B4))
        viewBinding.swipeLayout.setOnRefreshListener {
            if (viewModel.filterData != null) {
                if (isTable) {
                    viewModel.filterTable(viewModel.filterData!!)
                } else {
                    viewModel.filter(viewModel.filterData!!)
                }
            } else {
                if (isTable) {
                    viewModel.getTable()
                } else {
                    viewModel.getPositions()
                }
            }
        }
    }

    private inner class SpinnerInteractionListener : AdapterView.OnItemSelectedListener,
        View.OnTouchListener {
        var userSelect = false
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            userSelect = true
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
                spinnerAdapter.selectedItem = pos
                adapter.clear()
                showLoading()
                if (pos == 0) {
                    isTable = false
                    viewModel.tablePositions.value = arrayOf()
                    viewModel.getPositions()
                } else {
                    isTable = true
                    viewModel.positions.value = arrayOf()
                    viewModel.getTable()
                }
                spinnerAdapter.notifyDataSetChanged()
            }
        }
    }


    private val positionObserver = Observer<Array<Position>> { positions ->
        if (!isTable) {
            viewBinding.swipeLayout.isRefreshing = false
            hideLoading()
            adapter.clear()
            if (positions.isNotEmpty()) {
                adapter.addAll(
                    positions.map { position ->
                        PlaqueItem(position)
                    }
                )
            } else {
                adapter.add(TextViewItem("Ничего не найдено"))
            }
        }
    }

    private val tablePositionObserver = Observer<Array<MockPosition>> { positions ->
        if (isTable) {
            viewBinding.swipeLayout.isRefreshing = false
            hideLoading()
            adapter.clear()
            if (positions.isNotEmpty()) {
                adapter.add(
                    TableItem(number = "null")
                )
                adapter.addAll(
                    positions.map {
                        TableItem(
                            "null",
                            it,
                        )
                    }
                )
            } else {
                adapter.add(TextViewItem("Ничего не найдено"))
            }
        }
    }

    private fun initRecycler() {
        spinnerAdapter = SelectionAdapter(
            context!!,
            R.layout.item_text_view,
            arrayListOf("карточки", "таблица"),
        )
        viewBinding.apply {
            spinner.adapter = spinnerAdapter
            recycler.apply {
                adapter = this@ShowAllFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}