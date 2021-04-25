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
import com.andreev.skladapp.ui.shipment_history.ShipmentHistoryFragment
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
        toolbarBinding = viewBinding.toolbar.viewBinding
        viewBinding.toolbar.setUpDrawer(
            viewBinding.drawerLayout,
            viewBinding.viewDrawer.drawer,
            activity as MainActivity
        )
        viewModel.curMenuItem.observe(this, this)

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
            updateToolbar(R.string.see_all)
            closeDrawer()
        }

        viewBinding.viewDrawer.searchDi.setOnClickListener {
            launchChildFragment(SearchFragment())
            updateToolbar(R.string.search)
            closeDrawer()
        }

        viewBinding.viewDrawer.plavSearchDi.setOnClickListener {
            launchChildFragment(SearchPlavFragment())
            updateToolbar(R.string.search_plav)
            closeDrawer()
        }

        viewBinding.drawerLayout.unite_di.setOnClickListener {
            launchChildFragment(UniteFragment())
            updateToolbar(R.string.unite)
            closeDrawer()
        }

        viewBinding.drawerLayout.acceptance_di.setOnClickListener {
            launchChildFragment(GetFragment())
            updateToolbar(R.string.get)
            closeDrawer()
        }

        viewBinding.drawerLayout.shipment_di.setOnClickListener {
            launchChildFragment(ShipmentFragment())
            updateToolbar(R.string.ship)
            closeDrawer()
        }

        viewBinding.viewDrawer.logoutDi.setOnClickListener {
            logout()
        }
    }

    private fun loadUserData() {
        viewModel.loadUserFromLocal()
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
            val f = super.onBackPressed(containerId)
            if (childFragmentManager.backStackEntryCount > 1) {
                viewModel.curMenuItem.postValue(
                    childFragmentManager.findFragmentByTag
                        (
                        childFragmentManager.getBackStackEntryAt
                            (
                            childFragmentManager.backStackEntryCount - 2
                        ).name
                    )
                )
            } else {
                viewModel.curMenuItem.postValue(HubFragment())
            }
            return f
        }
    }

    override fun onChanged(fragment: Fragment?) {
        Timber.i(fragment?.tag)
        when (fragment) {
            is SearchFragment -> {
                updateToolbar(R.string.search)
            }
            is ShowAllFragment -> {
                updateToolbar(R.string.see_all)
            }
            is HistoryFragment -> {
                updateToolbar(R.string.history)
            }
            is SearchPlavFragment -> {
                updateToolbar(R.string.search_plav)
            }
            is ShipmentFragment -> {
                updateToolbar(R.string.ship)
            }
            is ShipmentHistoryFragment -> {
                updateToolbar(R.string.shipment_history)
            }
        }
    }

    private fun updateToolbar(
        @StringRes titleId: Int
    ) {
        toolbarBinding.tvToolbarTitle.setText(titleId)
    }

    private fun updateToolbar(
        title: String
    ) {
        toolbarBinding.tvToolbarTitle.text = title
    }

    private fun openDrawer() {
        viewBinding.toolbar.openDrawer(
            viewBinding.drawerLayout,
            viewBinding.viewDrawer.drawer
        )
        hideLoading()
    }

    fun logout() {
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
            viewModel.curMenuItem.postValue(fragment)
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