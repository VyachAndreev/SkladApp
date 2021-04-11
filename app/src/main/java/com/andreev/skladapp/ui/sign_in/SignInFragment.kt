package com.andreev.skladapp.ui.sign_in

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSignInBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SignInFragment: BaseFragment<FragmentSignInBinding>(), Observer<String> {
    lateinit var viewModel: SignInViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_sign_in

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.viewModel = viewModel
        viewBinding.btnSignIn.setOnClickListener {
            hideKeyBoard()
            signInUser()
        }

        viewModel.login.observe(this, this)
        viewModel.password.observe(this, this)
    }

    override fun onChanged(string: String?) {
        Timber.i("changed")
        viewModel.checkSingInAbility()
    }

    private fun signInUser() {
        var isSuccessSignIn: Boolean
        scopeMain.launch {
            showLoading()
            withContext(Dispatchers.IO) {
                isSuccessSignIn = viewModel.signUser()
            }
            hideLoading()

            if (isSuccessSignIn) {
                launchFragment(
                    R.id.fragment_container,
                    HubFragment(),
                    false,
                    replace = true,
                )
            }  else {
                showToast("Something went wrong")
            }
        }
    }
}