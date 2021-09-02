package com.andreev.skladapp.ui.hub

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentHubBinding
import com.andreev.skladapp.databinding.ViewToolbarBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui.MainActivity
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.get.GetFragment
import com.andreev.skladapp.ui.history.HistoryFragment
import com.andreev.skladapp.ui.search.SearchFragment
import com.andreev.skladapp.ui.search_plav.SearchPlavFragment
import com.andreev.skladapp.ui.shipment.ShipmentFragment
import com.andreev.skladapp.ui.show_all.ShowAllFragment
import com.andreev.skladapp.ui.sign_in.SignInFragment
import com.andreev.skladapp.ui.unite.UniteFragment
import kotlinx.android.synthetic.main.view_drawer.view.*
import timber.log.Timber

class HubFragment: BaseFragment<FragmentHubBinding>(), Observer<Fragment> {
    lateinit var viewModel: HubViewModel
    lateinit var toolbarBinding: ViewToolbarBinding

    override fun getLayoutRes(): Int = R.layout.fragment_hub

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(HubViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
        applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated")
        viewModel.curMenuItem.observe(this, this)
        toolbarBinding = viewBinding.toolbar.viewBinding
        viewBinding.toolbar.setUpDrawer(
            viewBinding.drawerLayout,
            viewBinding.viewDrawer.drawer,
            activity as MainActivity
        )

        launchChildFragment(
            R.id.hub_container,
            SearchFragment(),
            false,
            replace = true,
        )

        updateToolbar(R.string.search)

        viewBinding.viewDrawer.cross.setOnClickListener {
            closeDrawer()
        }

        viewBinding.viewDrawer.showAllDi.setOnClickListener {
            launchChildFragment(ShowAllFragment())
            closeDrawer()
        }

        viewBinding.viewDrawer.searchDi.setOnClickListener {
            launchChildFragment(SearchFragment())
            closeDrawer()
        }

        viewBinding.viewDrawer.plavSearchDi.setOnClickListener {
            launchChildFragment(SearchPlavFragment())
            closeDrawer()
        }

        viewBinding.drawerLayout.unite_di.setOnClickListener {
            launchChildFragment(UniteFragment())
            closeDrawer()
        }

        viewBinding.drawerLayout.shipment_history_di.setOnClickListener {
            launchChildFragment(HistoryFragment())
            closeDrawer()
        }

        viewBinding.drawerLayout.acceptance_di.setOnClickListener {
            launchChildFragment(GetFragment())
            closeDrawer()
        }

        viewBinding.drawerLayout.shipment_di.setOnClickListener {
            launchChildFragment(ShipmentFragment())
            closeDrawer()
        }

        viewBinding.viewDrawer.logoutDi.setOnClickListener {
            logout()
        }
    }

    override fun onBackPressed(containerId: Int): Boolean {
        return if (
            viewBinding.drawerLayout.isDrawerOpen(
                viewBinding.viewDrawer.drawer
            )
        ) {
            viewBinding.drawerLayout.closeDrawer(Gravity.LEFT)
            false
        } else {
            val last = childFragmentManager.fragments.last()
            if (last is ShipmentFragment && !last.isPred) {
                last.setPredLayout()
                return false
            }
            val f = super.onBackPressed(containerId)
            return f
        }
    }

    override fun onChanged(fragment: Fragment?) {
        Timber.i(fragment?.tag)
        when (fragment) {
            is ShowAllFragment -> {
                updateToolbar(R.string.show_all)
            }
            is SearchPlavFragment -> {
                updateToolbar(R.string.search_plav)
            }
            is SearchFragment -> {
                updateToolbar(R.string.search)
            }
            is UniteFragment -> {
                updateToolbar(R.string.unite)
            }
            is ShipmentFragment -> {
                updateToolbar(R.string.ship)
            }
            is HistoryFragment -> {
                updateToolbar(R.string.history)
            }
            is GetFragment -> {
                updateToolbar(R.string.get)
            }
        }
    }

    private fun updateToolbar(
        @StringRes titleId: Int
    ) {
        toolbarBinding.tvToolbarTitle.setText(titleId)
    }

    private fun updateToolbar(title: String    ) {
        toolbarBinding.tvToolbarTitle.text = title
    }

    private fun openDrawer() {
        viewBinding.toolbar.openDrawer(
            viewBinding.drawerLayout,
            viewBinding.viewDrawer.drawer
        )
        hideLoading()
    }

    private fun logout() {
        viewModel.logout()
        launchFragment(R.id.fragment_container, SignInFragment(), false)
    }

    fun launchChildFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        extras: Bundle? = null
    ) {
        if (childFragmentManager.fragments.last()::class != fragment::class) {
            super.launchChildFragment(
                R.id.hub_container,
                fragment,
                addToBackStack,
                extras,
                true
            )
        } else {
            viewBinding.drawerLayout.closeDrawer(Gravity.LEFT)
        }
    }

    private fun closeDrawer() {
        viewBinding.toolbar.closeDrawer(
            viewBinding.drawerLayout,
            viewBinding.viewDrawer.drawer
        )
    }

}