package com.goodchoice.android.ohneulen.ui.home

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.BuildConfig
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.repository.InitData
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.databinding.HomeBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.search.*
import com.goodchoice.android.ohneulen.util.*
import com.goodchoice.android.ohneulen.util.constant.ConstList
import org.koin.androidx.viewmodel.ext.android.viewModel

class Home() : Fragment(), OnBackPressedListener {

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
        //초기화 (두번씩 observe 되는것 방지)
//        searchViewModel.kakaoMapPoint= MutableLiveData()
//        searchViewModel.searchStoreList=MutableLiveData()

        //currentLocationSearch 초기화
        mainViewModel.currentLocationSearch = false
//        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_home
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (BuildConfig.DEBUG) {
            Toast.makeText(requireContext(), "개발용", Toast.LENGTH_LONG).show()
        }
        //문자열 색 입히기
        val title = resources.getString(R.string.home_title)
        val spanBuilder = SpannableStringBuilder(title)
        spanBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorOhneulen)),
            0,
            3,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanBuilder.setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.homeTitle.append(spanBuilder)



        binding.homeEditText.setOnEditorActionListener { v, actionId, _ ->
            if (v!!.id == R.id.home_editText && actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainViewModel.searchEditText = binding.homeEditText.text.toString()
                hideKeyboard(view, requireContext())
                MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map
            }

            return@setOnEditorActionListener false
        }

        binding.homeEditText.addTextChangedListener(object : TextWatcher {
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
        if (binding.homeEditText.text.isBlank()) {
            Toast.makeText(requireContext(), "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        mainViewModel.searchEditText = binding.homeEditText.text.toString()
        mainViewModel.currentLocationSearch = false
        hideKeyboard(view, requireContext())
        if (InitData.endNumber.value == 3)
            MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map
        else {
            InitData.endNumber.observe(viewLifecycleOwner, Observer {
                if (it == 3) {
                    MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map
                }
            })
        }
    }

    fun currentLocationClick(view: View) {
        mainViewModel.currentLocationSearch = true
        MainActivity.bottomNav.selectedItemId = R.id.menu_bottom_nav_map


        //임시
//        val imm =
//            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//        MainActivity.supportFragmentManager.beginTransaction().setCustomAnimations(
//            R.anim.enter_right_to_left,
//            R.anim.exit_left_to_right,
//            R.anim.exit_left_to_right,
//            R.anim.exit_left_to_right
//        ).add(MainActivity.mainFrameLayout.id, SearchFilter.newInstance())
//            .addToBackStack(null)
//            .commit()
//        replaceAppbarFragment(SearchFilterAppbar.newInstance())

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
