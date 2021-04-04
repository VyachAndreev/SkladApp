package com.andreev.skladapp.ui.show_all

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentShowAllBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._adapter.SelectionAdapter
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.utils.DialogUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

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
        initRecycler()
        showLoading()
        viewModel.positions.observe(this, positionListener)
        viewModel.getPositions()
        viewModel.getMarks()
        viewModel.getDiameter()
        viewBinding.openFilter.setOnClickListener {
            DialogUtils.showFilterDialog(
                context,
                viewModel.marks.value,
                viewModel.diameter.value,
                viewModel.packings.value,
            ) {

            }
        }
    }

    private val positionListener = Observer<Array<Position>> {
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
        spinnerAdapter = SelectionAdapter(context!!, R.layout.item_text_view,
            arrayListOf("Марка", "Диаметр", "Упаковка", "Партия", "Плавка", "Вес"))
        viewBinding.apply {
            spinner.adapter = spinnerAdapter
            recycler.apply {
                adapter = this@ShowAllFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}