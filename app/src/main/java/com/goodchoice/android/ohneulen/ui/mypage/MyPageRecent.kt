package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageRecentBinding
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.goodchoice.android.ohneulen.util.textColor
import timber.log.Timber

class MyPageRecent : Fragment() {

    companion object {
        fun newInstance() = MyPageRecent()
    }

    private lateinit var binding: MypageRecentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_recent,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //색 삽입
        val text = binding.mypageRecentEmptyTv.text.toString()
        Timber.e(text.toString())
        binding.mypageRecentEmptyTv.text =
            textColor(text, 5, 7, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
    }

    fun moveSearch(view: View) {
        replaceMainFragment(Search.newInstance())
        replaceAppbarFragment(SearchAppBar.newInstance())
    }
}