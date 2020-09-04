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
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.LikeBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class Like : Fragment() {

    companion object {
        fun newInstance() = Like()
    }

    private lateinit var binding: LikeBinding
    private val mainViewModel: MainViewModel by inject()
    private val likeViewModel: LikeViewModel by viewModel()

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
        val textColor =
            textColor(text, 5, 7, ContextCompat.getColor(requireContext(), R.color.colorOhneulen))
        textColor.setSpan(StyleSpan(Typeface.BOLD), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.likeEmptyTv.text =
            textColor

        binding.lifecycleOwner=viewLifecycleOwner
        binding.fragment = this
        binding.viewModel = likeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //찜목록 체크
        likeViewModel.getStoreLikeList()
        likeViewModel.likeStoreList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.likeRv.visibility = View.GONE
                binding.likeEmpty.visibility = View.VISIBLE
            } else {
                binding.likeRv.visibility = View.VISIBLE
                binding.likeEmpty.visibility = View.GONE
            }
        })
        //로그인 풀렸나 체크
        likeViewModel.loginCheck.observe(viewLifecycleOwner, Observer {
            if (!it) {
                loginDialog(requireContext(), LikeAppBar.newInstance(), true)
                likeViewModel.loginCheck.postValue(true)
            }
        })
    }

    fun moveSearch(view: View) {
        mainViewModel.searchEditText = "강남역"
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map
    }
}