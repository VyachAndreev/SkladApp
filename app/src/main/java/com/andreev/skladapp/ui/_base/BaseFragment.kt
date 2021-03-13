package com.andreev.skladapp.ui._base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.andreev.skladapp.R
import com.andreev.skladapp.SkladApplication
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {
    lateinit var viewBinding: T
    protected val scopeMain = CoroutineScope(Dispatchers.Main)

    abstract fun getLayoutRes(): Int

    abstract fun injectDependencies(applicationComponent: ApplicationComponent)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflateView(layoutInflater)
        return viewBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as? SkladApplication)?.let {
            injectDependencies(it.appComponent)
        }

    }

    protected fun launchFragment(
        @IdRes containerId: Int, fragment: Fragment,
        addToStack: Boolean,
        extras: Bundle? = null,
        replace: Boolean = false
    ) {
        (activity as? MainActivity)?.apply {
            this.launchFragment(containerId, fragment, addToStack, extras, replace)
        }
    }

    protected fun launchChildFragment(
        @IdRes containerId: Int,
        fragment: Fragment,
        addToStack: Boolean = true,
        extras: Bundle? = null,
        replace: Boolean = false
    ) {
        val ft = childFragmentManager.beginTransaction()
        fragment.arguments = extras

        if (replace)
            ft.replace(containerId, fragment, fragment.javaClass.simpleName)
        else
            ft.add(containerId, fragment, fragment.javaClass.simpleName)

        if (addToStack) {
            ft.addToBackStack(fragment.javaClass.simpleName)
        }
        Timber.i(fragment.tag)
        ft.commit()
    }

    private fun popChildFragment(
        @IdRes containerId: Int
    ) {
        val backEntry = childFragmentManager.getBackStackEntryAt(
            childFragmentManager.backStackEntryCount - 1
        )
        childFragmentManager.findFragmentByTag(backEntry.toString())?.let {
            childFragmentManager.beginTransaction().replace(
                containerId,
                it
            )
        }
        childFragmentManager.popBackStack()
    }

    open fun onBackPressed(
        @IdRes containerId: Int
    ): Boolean {
        Timber.i("onBackPressed")
        return if (childFragmentManager.backStackEntryCount == 0) {
            Timber.i("0")
            true
        } else {
            Timber.i("!=0")
            popChildFragment(containerId)
            false
        }
    }

    protected fun showLoading() {
        (activity as? MainActivity)?.apply {
            this.showLoading()
        }
    }

    protected fun hideLoading() {
        (activity as? MainActivity)?.apply {
            this.hideLoading()
        }
    }

    protected fun showToast(text: String) {
        (activity as? MainActivity)?.apply {
            this.showToast(text)
        }
    }

    protected fun showToast(@StringRes text: Int) {
        (activity as? MainActivity)?.apply {
            this.showToast(text)
        }
    }

    private fun inflateView(layoutInflater: LayoutInflater) {
        viewBinding = DataBindingUtil.inflate(
            layoutInflater, getLayoutRes(), null, false
        )
        viewBinding.lifecycleOwner = this
    }
}