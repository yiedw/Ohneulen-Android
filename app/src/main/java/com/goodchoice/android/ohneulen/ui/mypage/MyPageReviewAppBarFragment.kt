package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageReviewAppbarFragmentBinding
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class MyPageReviewAppBarFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageReviewAppBarFragment()
    }

    private lateinit var binding: MypageReviewAppbarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_review_appbar_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    fun backClick(view: View) {
        replaceAppbarFragment(MyPageAppBarFragment.newInstance())
        replaceMainFragment(MyPageFragment.newInstance())
    }

}