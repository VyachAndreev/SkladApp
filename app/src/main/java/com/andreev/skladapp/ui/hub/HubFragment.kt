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
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.search.SearchFragment
import com.andreev.skladapp.ui.search_plav.SearchPlavFragment
import com.andreev.skladapp.ui.sign_in.SignInFragment
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
            viewBinding.viewDrawer.drawer
        )
        viewModel.curMenuItem.observe(this, this)

        launchChildFragment(
            R.id.hub_container,
            SearchFragment(),
            replace = true,
        )

        updateToolbar(R.string.search)

        viewBinding.viewDrawer.cross.setOnClickListener {
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