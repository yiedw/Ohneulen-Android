package com.goodchoice.android.ohneulen.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.HomeAppbarBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPageAppBar
import com.goodchoice.android.ohneulen.ui.mypage.MyPage
import com.goodchoice.android.ohneulen.ui.home.noti.NotiAppBar
import com.goodchoice.android.ohneulen.ui.home.noti.Noti
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlin.system.exitProcess

class HomeAppBar :Fragment(),OnBackPressedListener{

    companion object{
        fun newInstance()=HomeAppBar()
    }

    private lateinit var binding:HomeAppbarBinding
    private val FINISH_TIME: Long = 3000
    private var backPressedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.home_appbar,
            container,
            false
        )
        binding.fragment=this
        MainActivity.appbarFrameLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MainActivity.appbarFrameLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorHeader))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun myClick(view: View){
        replaceAppbarFragment(MyPageAppBar.newInstance())
        replaceMainFragment(MyPage.newInstance())
    }

    fun notiClick(view:View){
        replaceAppbarFragment(NotiAppBar.newInstance())
        replaceMainFragment(Noti.newInstance())
    }

    override fun onBackPressed() {
        val tempTime: Long = System.currentTimeMillis();
        val intervalTime: Long = tempTime - backPressedTime
        if (intervalTime < FINISH_TIME) {
            requireActivity().finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toast.makeText(activity, "뒤로가기를 한번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
            backPressedTime = tempTime
        }
    }
}