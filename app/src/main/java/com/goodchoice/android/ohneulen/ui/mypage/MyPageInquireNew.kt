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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireNewBinding
import java.text.DecimalFormat

class MyPageInquireNew : Fragment() {
    companion object {
        fun newInstance() = MyPageInquireNew()
    }

    private lateinit var binding: MypageInquireNewBinding

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
        binding.mypageInquireNewEt1.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.mypageInquireNewEt1Length.text=binding.mypageInquireNewEt1.text.length.toString()
            }

        })

        //내용 글자수 세기
        binding.mypageInquireNewEt2.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length=binding.mypageInquireNewEt2.length()
                binding.mypageInquireNewEt2Length.text=length.toString()
            }

        })
    }
}