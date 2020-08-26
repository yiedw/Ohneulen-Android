package com.goodchoice.android.ohneulen.ui.like

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LikeAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.util.OnBackPressedListener

class LikeAppBar : Fragment(), OnBackPressedListener {

    companion object {
        fun newInstance() = LikeAppBar()
    }

    private lateinit var binding: LikeAppbarBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        //store stat 설정
        StoreAppBar.stat = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.like_appbar,
            container,
            false
        )
        return binding.root
    }

    override fun onBackPressed() {
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
    }
}