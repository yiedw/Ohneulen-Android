package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageInquire : Fragment() {

    companion object {
        fun newInstance() = MyPageInquire()
    }

    private lateinit var binding: MypageInquireBinding

    private val mypageViewModel: MyPageViewModel by viewModel()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mypageViewModel.getInquireList()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire,
            container,
            false
        )
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mypageViewModel

        binding.mypageInquireRv.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            //스와이프 기능
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }

            //클릭했을때
            override fun onSingleTab(motionEvent: MotionEvent) {
                super.onSingleTab(motionEvent)
                val child = binding.mypageInquireRv.findChildViewUnder(motionEvent.x, motionEvent.y)
                val inquireDetailView = child!!.findViewById<LinearLayout>(R.id.inquire_item_detail)
                if (inquireDetailView.visibility == View.GONE) {
                    inquireDetailView.visibility = View.VISIBLE
                } else {
                    inquireDetailView.visibility = View.GONE
                }
//                val adapterPosition = binding.mypageInquireRv.getChildAdapterPosition(child!!)
            }
        })

        //문의내역이 비어있을경우
        binding.mypageInquire.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            //스와이프 기능
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mypageViewModel.mypageInquireList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.mypageInquireRv.visibility = View.GONE
                binding.mypageInquireEmpty.visibility = View.VISIBLE
            } else {
                binding.mypageInquireRv.visibility = View.VISIBLE
                binding.mypageInquireEmpty.visibility = View.GONE
            }

        })

        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (!it) {
                loginDialog(requireContext(), false)
            }
        })
    }


    fun newClick(view: View) {
        replaceAppbarFragment(MyPageInquireNewAppBar.newInstance())
        popupFragment(MyPageInquireNew.newInstance())
    }

}