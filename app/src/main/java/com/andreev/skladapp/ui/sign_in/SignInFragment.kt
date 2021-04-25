package com.andreev.skladapp.ui.sign_in

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.TestBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment


class SignInFragment : BaseFragment<TestBinding>()/*, Observer<String>*/ {
    lateinit var viewModel: SignInViewModel
    private val URL = "http://ferro-trade.ru/login?logout"

    override fun getLayoutRes(): Int = R.layout.test

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        viewBinding.viewModel = viewModel
//        viewBinding.btnSignIn.setOnClickListener {
//            hideKeyBoard()
//            signInUser()
//        }
//
//        viewModel.login.observe(this, this)
//        viewModel.password.observe(this, this)
        openInWebView(URL)

        viewBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("http://ferro-trade.ru/login")) {
                    openInWebView("http://ferro-trade.ru/login?error")
                    return true
                } else {
                    launchFragment(
                        R.id.fragment_container,
                        HubFragment(),
                        false,
                        replace = true,
                    )
                    return true
                }
            }
        }
    }


    private fun openInWebView(url: String) {
        viewBinding.webView.loadUrl(url)
    }


//    override fun onChanged(string: String?) {
//        Timber.i("changed")
//        viewModel.checkSingInAbility()
//    }
//
//    private fun signInUser() {
//        var isSuccessSignIn: Boolean
//        scopeMain.launch {
//            showLoading()
//            withContext(Dispatchers.IO) {
//                isSuccessSignIn = viewModel.signUser()
//            }
//            hideLoading()
//
//            if (isSuccessSignIn) {
//                launchFragment(
//                    R.id.fragment_container,
//                    HubFragment(),
//                    false,
//                    replace = true,
//                )
//            }  else {
//                showToast("Something went wrong")
//            }
//        }
//    }
}