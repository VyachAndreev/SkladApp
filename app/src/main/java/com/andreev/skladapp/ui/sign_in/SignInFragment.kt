package com.andreev.skladapp.ui.sign_in

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProviders
import com.andreev.skladapp.R
import com.andreev.skladapp.databinding.FragmentSignInBinding
import com.andreev.skladapp.di.ApplicationComponent
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment


class SignInFragment : BaseFragment<FragmentSignInBinding>(){
    lateinit var viewModel: SignInViewModel
    private val URL = "http://ferro-trade.ru/login?logout"

    override fun getLayoutRes(): Int = R.layout.fragment_sign_in

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        viewModel.injectDependencies(applicationComponent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        openInWebView(URL)

        viewBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return return if (url.startsWith("http://ferro-trade.ru/login")) {
                    openInWebView(url)
                    true
                } else {
                    launchFragment(
                        R.id.fragment_container,
                        HubFragment(),
                        false,
                        replace = true,
                    )
                    true
                }
            }
        }
    }


    private fun openInWebView(url: String) {
        viewBinding.webView.loadUrl(url)
    }
}