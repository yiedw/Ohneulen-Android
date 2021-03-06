package com.goodchoice.android.ohneulen.ui.mypage

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageRecentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
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
        val textColor=textColor(text, 5, 7, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
        textColor.setSpan(StyleSpan(Typeface.BOLD),5,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.mypageRecentEmptyTv.text =
            textColor
    }

    fun moveSearch(view: View) {
        MainActivity.bottomNav.selectedItemId=R.id.menu_bottom_nav_map
    }
}