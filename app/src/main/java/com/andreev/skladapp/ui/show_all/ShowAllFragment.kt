package com.andreev.skladapp.ui.show_all

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.Constants
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentShowAllBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._adapter.SelectionAdapter
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.information.InformationFragment
import com.andreev.skladapp.ui.utils.DialogUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class ShowAllFragment: BaseFragment<FragmentShowAllBinding>() {

    private var adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var spinnerAdapter: SelectionAdapter<String>
    lateinit var viewModel: ShowAllViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_show_all

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(ShowAllViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter.setOnItemClickListener { item, view ->
            when (item) {
                is PlaqueItem -> {
                    val args = Bundle()
                    item.pos.id?.let { args.putLong(Constants.ID, it) }
                    Timber.i(item.pos.type)
                    args.putBoolean(Constants.ISPACKAGE, item.pos.type == "POSITION")
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

        initRecycler()
        showLoading()
        viewModel.positions.observe(this, positionListener)
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
                viewModel.filter(it)
            }
        }

        viewBinding.swipeLayout.setColorSchemeColors(resources.getColor(R.color.blue_3B4))
        viewBinding.swipeLayout.setOnRefreshListener {
            if (viewModel.filterData != null) {
                viewModel.filter(viewModel.filterData!!)
            } else {
                viewModel.getPositions()
            }
        }
    }

    private val positionListener = Observer<Array<Position>> {
        viewBinding.swipeLayout.isRefreshing = false
        hideLoading()
        adapter.clear()
        if (it.isNotEmpty()) {
            adapter.addAll(
                it.map { position ->
                    PlaqueItem(position)
                }
            )
        } else {
            adapter.add(TextViewItem("Ничего не найдено"))
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