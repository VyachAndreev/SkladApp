package com.andreev.skladapp.ui.hub

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentHubBinding
import com.andreev.skladapp.databinding.ViewToolbarBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
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
    }

}