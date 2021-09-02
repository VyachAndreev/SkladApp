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
import com.andreev.skladapp.SkladApplication
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

abstract class BaseFragment<T: ViewDataBinding> : Fragment() {
    protected val fm by lazy { childFragmentManager }
    protected lateinit var viewBinding: T
    protected val scopeMain = CoroutineScope(Dispatchers.Main)

    abstract fun getLayoutRes(): Int

    abstract fun injectDependencies(applicationComponent: ApplicationComponent)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as? SkladApplication)?.let {
            injectDependencies(it.appComponent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflateView(layoutInflater)
        return viewBinding.root
    }

    private fun inflateView(layoutInflater: LayoutInflater) {
        viewBinding = DataBindingUtil.inflate(
            layoutInflater, getLayoutRes(), null, false
        )
        viewBinding.lifecycleOwner = this
    }

    private fun popChildFragment(@IdRes containerId: Int) {
        with(fm) {
            val backEntry = getBackStackEntryAt(backStackEntryCount - 1)
            findFragmentByTag(backEntry.toString())?.let {
                beginTransaction().replace(containerId, it)
            }
            popBackStack()
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
        val ft = fm.beginTransaction()
        with(fragment) {
            arguments = extras
            if (replace)
                ft.replace(containerId, this, javaClass.simpleName)
            else
                ft.add(containerId, this, javaClass.simpleName)

            if (addToStack) {
                ft.addToBackStack(javaClass.simpleName)
            }
            Timber.i(tag)
        }
        ft.commit()
    }

    protected fun hideKeyBoard() {
        (activity as? MainActivity)?.hideKeyboard()
    }

    protected fun showLoading() {
        (activity as? MainActivity)?.showLoading()
    }

    protected fun hideLoading() {
        (activity as? MainActivity)?.hideLoading()
    }

    protected fun showToast(text: String) {
        (activity as? MainActivity)?.showToast(text)
    }

    protected fun showToast(@StringRes text: Int) {
        (activity as? MainActivity)?.showToast(text)
    }

    protected fun openUrl(url: String) {
        (activity as? MainActivity)?.openUrl(url)
    }

    open fun onBackPressed(
        @IdRes containerId: Int
    ): Boolean {
        Timber.i("onBackPressed")
        return if (fm.backStackEntryCount == 0) {
            Timber.i("stack is empty")
            true
        } else {
            Timber.i("stack is not empty")
            popChildFragment(containerId)
            false
        }
    }
}