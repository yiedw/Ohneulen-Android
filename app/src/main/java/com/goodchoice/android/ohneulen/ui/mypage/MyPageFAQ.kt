package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageFaqBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.adapter.FAQAdapter
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MyPageFAQ : Fragment() {
    companion object {
        fun newInstance() = MyPageFAQ()
    }

    private lateinit var binding: MypageFaqBinding
    private val myPageViewModel: MyPageViewModel by viewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_faq,
            container,
            false
        )
        binding.viewModel = myPageViewModel
        binding.lifecycleOwner = this

        binding.mypageFaqRv.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            //스와이프 기능
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }

            //rv클릭 기능
            //밖으로 뺀 이유: adapter 안에 있으면 swipe 기능을 사용할수가 없음
            override fun onSingleTab(motionEvent: MotionEvent) {
                super.onSingleTab(motionEvent)
                //position 을 직접 찾아줌
                //adapter 안에서 clickListener 을 쓸 경우 swipe 기능 사용 불가
                val adapter = binding.mypageFaqRv.adapter as FAQAdapter
                val child = binding.mypageFaqRv.findChildViewUnder(motionEvent.x, motionEvent.y)
                val adapterPosition = binding.mypageFaqRv.getChildAdapterPosition(child!!)

                //클릭한 항목 제외하고 체크상태를 다 기본으로 되돌림
                for (i in myPageViewModel.mypageFAQList.value!!.indices) {
                    if (i != adapterPosition && myPageViewModel.mypageFAQList.value!![i].check) {
                        myPageViewModel.mypageFAQList.value!![i].check = false
//                        adapter.notifyItemChanged(i)
                    }
                }
                //기존에 체크되어있던 리스트를 풀어줌
                myPageViewModel.mypageFAQList.value!![adapterPosition].check =
                    !myPageViewModel.mypageFAQList.value!![adapterPosition].check
//                adapter.notifyItemChanged(adapterPosition)
                //깜빡거리는 현상을 없애기위해 아이템에 id를줌
                //notifyItemChanged()를 안쓴이유 -> 이미지가 순간적으로 겹쳐보이는 현상
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //바텀바 숨기기
        val animation = AlphaAnimation(0f, 1f)
        MainActivity.bottomNav.visibility = View.GONE
        MainActivity.bottomNav.animation = animation
    }

    override fun onDestroy() {
        super.onDestroy()
        //뒤로가면 체크되있던 항목들 다 풀어주기 (리셋)
        for (i in myPageViewModel.mypageFAQList.value!!) {
            i.check = false
        }
    }

}