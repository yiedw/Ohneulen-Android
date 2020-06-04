package com.goodchoice.android.ohneulen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.ui.home.HomeMainFragment
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var supportFragmentManager:FragmentManager
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Companion.supportFragmentManager=supportFragmentManager

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, HomeMainFragment.newInstance()).commitNow()
            supportFragmentManager.beginTransaction()
                .replace(R.id.appbar_fragment, HomeAppBarFragment.newInstance()).commitNow()
        }
    }
}
