package com.andreev.skladapp.ui.hub

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.GravityCompat
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
import timber.log.Timber

class HubFragment : BaseFragment<FragmentHubBinding>(), Observer<Fragment> {
    lateinit var viewModel: HubViewModel
    private lateinit var toolbarBinding: ViewToolbarBinding

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
            activity as MainActivity,
        )
        launchChildFragment(
            R.id.hub_container,
            SearchFragment(),
            false,
            replace = true,
        )
        updateToolbar(R.string.search)
        with(viewBinding.viewDrawer) {
            cross.setOnClickListener { closeDrawer() }
            showAllDi.setOnClickListener {
                launchChildFragment(ShowAllFragment())
                closeDrawer()
            }
            searchDi.setOnClickListener {
                launchChildFragment(SearchFragment())
                closeDrawer()
            }
            plavSearchDi.setOnClickListener {
                launchChildFragment(SearchPlavFragment())
                closeDrawer()
            }

            uniteDi.setOnClickListener {
                launchChildFragment(UniteFragment())
                closeDrawer()
            }
            shipDi.setOnClickListener {
                launchChildFragment(ShipmentFragment())
                closeDrawer()
            }
            historyDi.setOnClickListener {
                launchChildFragment(HistoryFragment())
                closeDrawer()
            }
            getDi.setOnClickListener {
                launchChildFragment(GetFragment())
                closeDrawer()
            }
            logoutDi.setOnClickListener { logout() }
        }
    }

    override fun onBackPressed(containerId: Int): Boolean {
        with(viewBinding.drawerLayout) {
            return if (isDrawerOpen(viewBinding.viewDrawer.drawer)) {
                closeDrawer(GravityCompat.START)
                false
            } else {
                val last = fm.fragments.last()
                if (last is ShipmentFragment && !last.isPred) {
                    last.setPredLayout()
                    return false
                }
                return super.onBackPressed(containerId)
            }
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

    private fun updateToolbar(@StringRes titleId: Int) {
        toolbarBinding.tvToolbarTitle.setText(titleId)
    }

    private fun openDrawer() {
        with(viewBinding) {
            toolbar.openDrawer(
                drawerLayout,
                viewDrawer.drawer
            )
        }
        hideLoading()
    }

    private fun closeDrawer() {
        with(viewBinding) {
            toolbar.closeDrawer(
                drawerLayout,
                viewDrawer.drawer
            )
        }
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
        if (fm.fragments.last()::class != fragment::class) {
            super.launchChildFragment(
                R.id.hub_container,
                fragment,
                addToBackStack,
                extras,
                true,
            )
        } else {
            viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}