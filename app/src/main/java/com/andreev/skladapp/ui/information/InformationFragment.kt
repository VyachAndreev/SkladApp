package com.andreev.skladapp.ui.information

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreev.skladapp.Constants
import com.andreev.skladapp.R
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.databinding.FragmentInformationBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui._item.PlaqueItem
import com.andreev.skladapp.ui._item.TextViewItem
import com.andreev.skladapp.ui.utils.DialogUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import timber.log.Timber

class InformationFragment: BaseFragment<FragmentInformationBinding>() {
    var adapter = GroupAdapter<GroupieViewHolder>()
    lateinit var viewModel: InformationViewModel
    private var isPackage: Boolean? = null
    private var id: Long? = null

    override fun getLayoutRes(): Int = R.layout.fragment_information

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(InformationViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            isPackage = it.getBoolean(Constants.ISPACKAGE)
            id = it.getLong(Constants.ID)
            Timber.i("$arguments")
        }

        viewModel.item.observe(this, itemListener)

        viewBinding.shipBtn.setOnClickListener {
            val weight = viewModel.item.value?.mass
            DialogUtils.showShipDialog(context, weight) {
                viewModel.departure(it)
            }
        }

        showLoading()

        isPackage?.let {
            if (!it) {
                setPositionLayout()
                showLoading()
                viewModel.getPosition(id)
            } else {
                setPackageLayout()
                viewModel.getPackage(id)
            }
        }
    }

    private fun setPositionLayout() {
        viewBinding.apply {
            showPositions.visibility = View.GONE
            if (viewBinding.position.status == "Departured") {
                layoutButtons.visibility = View.GONE
            }
        }
    }

    private fun setPackageLayout() {
        viewBinding.apply {
            layoutButtons.visibility = View.GONE
            initRecycler()
            showPositions.setOnClickListener {
                if (recycler.visibility == View.VISIBLE) {
                    recycler.visibility = View.GONE
                } else {
                    recycler.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initRecycler() {
        viewBinding.recycler.apply {
            adapter = this@InformationFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private val itemListener = Observer<Position> {
        hideLoading()
        viewBinding.position = viewModel.item.value
        isPackage?.let {
            if (it) {
                if (viewModel.item.value?.positionsList?.size!! > 0) {
                    viewModel.item.value?.positionsList?.map { position ->
                        PlaqueItem(
                            position
                        )
                    }?.let { it1 ->
                        adapter.addAll(
                            it1
                        )
                    }
                } else {
                    adapter.add(
                        TextViewItem("В поддоне нет позиций")
                    )
                }
            }
        }
    }
}