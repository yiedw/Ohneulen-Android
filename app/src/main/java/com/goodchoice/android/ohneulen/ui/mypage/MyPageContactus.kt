package com.goodchoice.android.ohneulen.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageContactusBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class MyPageContactus : Fragment() {
    companion object {
        fun newInstance() = MyPageContactus()
    }

    private lateinit var binding: MypageContactusBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainActivity.bottomNav.visibility = View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_contactus,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mypageContactus.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }

        })


        binding.mypageContactusTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.mypageContactusTitleLength.text =
                    binding.mypageContactusTitle.text.length.toString()
            }

        })

        binding.mypageContactusContents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apply {
                    mypageContactusContentsLength.text =
                        binding.mypageContactusContents.text.length.toString()
                    mypageContactusContentsLength2.text =
                        binding.mypageContactusContents.text.length.toString()
                }
            }

        })

        moveTerm3()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
    }

    private fun moveTerm3() {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                replaceAppbarFragment(MyPageTerms3AppBar.newInstance())
                addMainFragment(MyPageTerms3.newInstance(), true)
            }
        }
        val text = SpannableString("개인정보 수집 및 이용")
        text.setSpan(clickableSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(UnderlineSpan(), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val mText = TextUtils.concat("오늘은 ", text, "의 내용을 확인하였으며, 이에 동의합니다")
        binding.mypageContactusFooter.isClickable = true
        binding.mypageContactusFooter.movementMethod = LinkMovementMethod.getInstance()
        binding.mypageContactusFooter.text = mText

    }

}