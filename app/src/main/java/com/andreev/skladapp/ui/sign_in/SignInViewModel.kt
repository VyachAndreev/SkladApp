package com.andreev.skladapp.ui.sign_in

import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseViewModel

class SignInViewModel: BaseViewModel() {

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

}