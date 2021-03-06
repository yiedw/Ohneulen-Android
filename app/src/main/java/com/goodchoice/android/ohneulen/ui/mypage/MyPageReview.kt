package com.goodchoice.android.ohneulen.ui.mypage

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageReviewBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageReview : Fragment() {
    companion object {
        fun newInstance() = MyPageReview()
    }

    private lateinit var binding: MypageReviewBinding
    private val mypageViewModel: MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_review,
            container,
            false
        )
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = mypageViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MyPageReviewAdapter()
        binding.mypageReviewRv.adapter = adapter

        mypageViewModel.mypageReviewList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                reviewEmptyText()
            }else{
                binding.mypageLikeZero.visibility=View.GONE
            }
        })
    }

    private fun reviewEmptyText() {
        val textColor = textColor("맛집", 0, 2, requireContext().getColor(R.color.colorOhneulen))
        textColor.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            textColor.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val text = TextUtils.concat("아직 작성한 후기가 없어요\n지금 나만의 ", textColor, "을 공유하시겠어요?")
        binding.mypageReviewEmptyTv.text = text
        binding.mypageLikeZero.visibility=View.GONE
    }

    fun moveSearch(view: View) {
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map
    }


}