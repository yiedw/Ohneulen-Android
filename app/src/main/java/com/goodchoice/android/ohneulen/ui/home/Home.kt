package com.goodchoice.android.ohneulen.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.databinding.HomeBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.search.SearchAppBar
import com.goodchoice.android.ohneulen.ui.search.Search
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class Home() : Fragment(),OnBackPressedListener {

    companion object {
        fun newInstance() = Home()
    }

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: HomeBinding

    private val FINISH_TIME: Long = 3000
    private var backPressedTime: Long = 0
    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home,
            container,
            false
        )
        binding.fragment = this
//        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //문자열 색 입히기
        val title = resources.getString(R.string.home_title)
        val spanBuilder = SpannableStringBuilder(title)
        spanBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorOhneulen)),
            0,
            3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.homeTitle.append(spanBuilder)



        binding.homeEditText.setOnEditorActionListener { v, actionId, _ ->
            if (v!!.id == R.id.home_editText && actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainViewModel.searchEditText = binding.homeEditText.text.toString()
                replaceAppbarFragment(SearchAppBar.newInstance())
                replaceMainFragment(Search.newInstance(), tag = "search")
            }

            return@setOnEditorActionListener false
        }

        binding.homeEditText.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (binding.homeEditText.text.toString().isEmpty()) {
                    binding.homeClear.visibility = View.GONE
                } else {
                    binding.homeClear.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun searchOnClick(view: View) {
        mainViewModel.searchEditText = binding.homeEditText.text.toString()
        replaceAppbarFragment(SearchAppBar.newInstance())
        replaceMainFragment(Search.newInstance(), tag = "search")
    }

    fun currentLocationClick(view: View) {
        mainViewModel.searchEditText = ConstList.CURRENT_LOCATION
        replaceAppbarFragment(SearchAppBar.newInstance())
        replaceMainFragment(Search.newInstance())
    }

    override fun onBackPressed() {
        val tempTime: Long = System.currentTimeMillis();
        val intervalTime: Long = tempTime - backPressedTime
        if (intervalTime < FINISH_TIME) {
            requireActivity().finish()
        } else {
            Toast.makeText(activity, "뒤로가기를 한번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
            backPressedTime = tempTime
        }
    }


}
