package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireNewBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.DecimalFormat

class MyPageInquireNew : Fragment() {
    companion object {
        fun newInstance() = MyPageInquireNew()
    }

    private lateinit var binding: MypageInquireNewBinding
    private val mypageViewModel: MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire_new,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //뒤에 터치 안먹게하기
        binding.mypageInquireNew.setOnTouchListener { v, event -> true }

        val spinner = binding.mypageInquireNewSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.inquire_spinner_item,
            R.layout.mypage_inquire_new_spinner
        ).also { it ->
            it.setDropDownViewResource(R.layout.mypage_inquire_new_spinner_array)
            spinner.adapter = it
        }

        //제목 글자수 세기
        binding.mypageInquireNewEt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.mypageInquireNewEt1Length.text =
                    binding.mypageInquireNewEt1.text.length.toString()
            }

        })

        //내용 글자수 세기
        binding.mypageInquireNewEt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = binding.mypageInquireNewEt2.length()
                binding.mypageInquireNewEt2Length.text = length.toString()
            }

        })

        //뷰모델 데이터 관찰
        mypageViewModel.mypageInquireCode.observe(viewLifecycleOwner, Observer {
            if (it == ConstList.SUCCESS) {
                Toast.makeText(requireContext(), "문의가 접수되었습니다", Toast.LENGTH_SHORT).show()
                mypageViewModel.mypageInquireCode.postValue("")
                replaceAppbarFragment(MyPageInquireAppBar.newInstance())
                MainActivity.supportFragmentManager.popBackStack()
            } else if (it == ConstList.REQUIRE_LOGIN) {
                loginDialog(requireContext(), MyPageInquireAppBar.newInstance(), false)
            }
        })
    }

    fun submitClick(view: View) {
        var gubun1 = (binding.mypageInquireNewSpinner.selectedItemPosition + 1).toString()
        while (gubun1.length != 3) {
            gubun1 = "0$gubun1"
        }
        val title = binding.mypageInquireNewEt1.text.toString()
        val contents = binding.mypageInquireNewEt2.text.toString()
        if (title.isEmpty() || contents.isEmpty()) {
            Toast.makeText(requireContext(), "제목 또는 내용을 적어주세요", Toast.LENGTH_SHORT).show()
            return
        }
        mypageViewModel.setInquireList(gubun1, title, contents)

    }
}