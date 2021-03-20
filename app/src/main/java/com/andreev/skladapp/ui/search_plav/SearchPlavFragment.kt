package com.andreev.skladapp.ui.search_plav

import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui.search.SearchFragment

class SearchPlavFragment: SearchFragment() {

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SearchPlavViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }
}