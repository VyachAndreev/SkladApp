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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class InformationFragment : BaseFragment<FragmentInformationBinding>() {
    private lateinit var viewModel: InformationViewModel
    private var adapter = GroupAdapter<GroupieViewHolder>()
    private var isPackage: Boolean? = null
    private var id: Long? = null

    override fun getLayoutRes(): Int = R.layout.fragment_information

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(InformationViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            isPackage = it.getBoolean(Constants.IS_PACKAGE)
            id = it.getLong(Constants.ID)
            Timber.i("$arguments")
        }
        with(viewModel) {
            item.observe(this@InformationFragment, itemListener)
            departureResponse.observe(this@InformationFragment, departureObserver)
            viewBinding.shipBtn.setOnClickListener {
                val weight = item.value?.mass
                DialogUtils.showShipDialog(context, weight) {
                    showLoading()
                    departure(it)
                }
            }
            showLoading()
            isPackage?.let {
                if (!it) {
                    setPositionLayout()
                    getPosition(id)
                } else {
                    setPackageLayout()
                    getPackage(id)
                }
            }
        }
    }

    private fun setPositionLayout() {
        viewBinding.showPositions.visibility = gone
    }

    private fun setPackageLayout() {
        with(viewBinding) {
            layoutButtons.visibility = gone
            initRecycler()
            showPositions.setOnClickListener {
                with(recycler) {
                    visibility = if (visibility == visible) {
                        gone
                    } else {
                        visible
                    }
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
        with(viewBinding) {
            position = viewModel.item.value
            if (viewBinding.position?.status != DEPARTURED && !isPackage!!) {
                layoutButtons.visibility = visible
            }
        }
        isPackage?.let {
            if (it) {
                if (viewModel.item.value?.positionsList?.size!! > 0) {
                    viewModel.item.value?.positionsList?.map { position -> PlaqueItem(position) }
                        ?.let { plaque -> adapter.addAll(plaque) }
                } else { adapter.add(TextViewItem(getString(R.string.item_no_positions))) }
            }
        }
    }

    private val departureObserver = Observer<String> {
        viewBinding.layoutButtons.visibility = View.GONE
        isPackage?.let {
            with(viewModel) {
                if (!it) {
                    getPosition(id)
                } else {
                    getPackage(id)
                }
            }
        }
    }

    companion object {
        private const val DEPARTURED = "Departured"
    }
}