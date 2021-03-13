package com.andreev.skladapp.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.andreev.skladapp.R
import com.andreev.skladapp.SkladApplication
import com.andreev.skladapp.databinding.ActivityMainBinding
import com.andreev.skladapp.stored_data.UserStoredData
import com.andreev.skladapp.ui._base.BaseFragment
import com.andreev.skladapp.ui.hub.HubFragment
import com.andreev.skladapp.ui.sign_in.SignInFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userData: UserStoredData

    lateinit var viewBinding: ActivityMainBinding
    val scopeMain = CoroutineScope(Dispatchers.Main)
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SkladApplication).appComponent.inject(this)
        setContentView(R.layout.activity_main)
        viewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        progressBar = viewBinding.progressCircular
        checkUser()
    }

    fun checkUser() {
        launchFragment(R.id.fragment_container, SignInFragment(), false)
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
    
    fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes text: Int) {
        Toast.makeText(applicationContext, getText(text), Toast.LENGTH_SHORT).show()
    }

}