package com.andreev.skladapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.andreev.skladapp.R
import com.andreev.skladapp.SkladApplication
import com.andreev.skladapp.databinding.ActivityMainBinding
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.sign_in.SignInFragment
import com.andreev.skladapp.ui.utils.ResourceProvider
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userData: UserStoredData

    private lateinit var viewBinding: ActivityMainBinding
    val visible by lazy { View.VISIBLE }
    val gone by lazy { View.GONE }
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SkladApplication).appComponent.inject(this)
        ResourceProvider.setContext(this)
        setContentView(R.layout.activity_main)
        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        progressBar = viewBinding.progressCircular
        launchFragment(R.id.fragment_container, SignInFragment(), false)
    }

    fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun launchFragment(
        @IdRes container: Int,
        fragment: Fragment,
        addToBackStack: Boolean,
        args: Bundle? = null,
        replace: Boolean = false,
    ) {
        val ft = supportFragmentManager.beginTransaction()
        fragment.arguments = args

        if (replace) {
            ft.replace(container, fragment, fragment.javaClass.simpleName)
        } else {
            ft.add(container, fragment, fragment.javaClass.simpleName)
        }
        if (addToBackStack) {
            ft.addToBackStack(fragment.javaClass.simpleName)
        }
        ft.commit()
    }

    fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        (fragment as? BaseFragment<*>)?.apply {
            if (this.onBackPressed(R.id.fragment_container)) {
                super.onBackPressed()
            }
        }
    }

    fun openUrl(url: String) {
        Timber.i(url)
        val start = getString(R.string.util_url_start)
        if (!url.startsWith(start)) {
            openUrl("$start$url")
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    
    fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes text: Int) {
        Toast.makeText(applicationContext, getText(text), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        ResourceProvider.clearContext()
        super.onDestroy()
    }
}