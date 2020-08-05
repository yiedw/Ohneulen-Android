package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageReviewBinding
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageReview : Fragment() {
    companion object {
        fun newInstance() = MyPageReview()
    }

    private lateinit var binding: MypageReviewBinding
    private val mypageViewModel :MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_review,
            container,
            false
        )
        binding.fragment=this
        binding.lifecycleOwner=this
        binding.viewModel=mypageViewModel
        return binding.root
    }

    fun moveSearch(view: View) {
        replaceMainFragment(Search.newInstance())
        replaceAppbarFragment(SearchAppBar.newInstance())
    }


}