package com.andreev.skladapp.ui._base

import androidx.lifecycle.ViewModel
import com.andreev.skladapp.di.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel: ViewModel() {
    protected val scopeMain by lazy { CoroutineScope(Dispatchers.Main) }
    protected val scopeIO by lazy { CoroutineScope(Dispatchers.IO)}

    abstract fun injectDependencies(applicationComponent: ApplicationComponent)

    override fun onCleared() {
        super.onCleared()
        scopeMain.cancel()
        scopeIO.cancel()
    }
}