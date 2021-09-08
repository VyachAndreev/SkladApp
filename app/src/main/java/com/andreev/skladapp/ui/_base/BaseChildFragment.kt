package com.andreev.skladapp.ui._base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.andreev.skladapp.ui.hub.HubFragment

abstract class BaseChildFragment<T: ViewDataBinding>: BaseFragment<T>() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (parentFragment as HubFragment).viewModel.curMenuItem.value = this
    }
}