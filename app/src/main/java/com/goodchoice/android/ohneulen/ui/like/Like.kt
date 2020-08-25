package com.goodchoice.android.ohneulen.ui.like

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LikeBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class Like : Fragment() {

    companion object{
        fun newInstance()=Like()
    }
    private lateinit var binding: LikeBinding
    private val mainViewModel:MainViewModel by inject()

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.like,
            container,
            false
        )
        val text = binding.likeEmptyTv.text.toString()
        val textColor=textColor(text, 5, 7, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
        textColor.setSpan(StyleSpan(Typeface.BOLD),5,7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.likeEmptyTv.text =
            textColor

        binding.fragment=this
        return binding.root
    }

    fun moveSearch(view: View) {
        mainViewModel.searchEditText="강남역"
        MainActivity.bottomNav.selectedItemId=R.id.menu_bottom_nav_map
    }
}