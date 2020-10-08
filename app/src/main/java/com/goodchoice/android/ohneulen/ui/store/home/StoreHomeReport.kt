package com.goodchoice.android.ohneulen.ui.store.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeReportBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.android.ext.android.inject

class StoreHomeReport(private val storeName: String) : Fragment() {
    companion object {
        fun newInstance(storeName: String) = StoreHomeReport(storeName)
    }

    private lateinit var binding: StoreHomeReportBinding
    private val storeViewModel: StoreViewModel by inject()

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home_report,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportTitle()

        binding.storeHomeReportContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = binding.storeHomeReportContent.text.toString().length
                binding.storeReportEtLength.text = length.toString()
            }

        })

        //작성완료 버튼을 누른후 데이터를 받아옴(성공인지 실패인지)
        storeViewModel.networkResultCode.observe(viewLifecycleOwner, Observer {
            if (it == ConstList.SUCCESS) {
                Toast.makeText(requireContext(), "신고가 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                replaceAppbarFragment(StoreAppBar.newInstance())
                MainActivity.supportFragmentManager.popBackStack()
            } else if (it == ConstList.REQUIRE_LOGIN) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                loginDialog(requireContext(), false)

            } else {
                Toast.makeText(requireContext(), "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reportTitle() {
        val textColor = textColor(
            storeName,
            0,
            storeName.length,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )
        textColor.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            storeName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val mStoreName = TextUtils.concat(
            textColor, "에서 \n정정해야 할 내용이 있나요?"
        )
        binding.storeHomeReportTitle.text = mStoreName

    }


    fun submitOnClick(view: View) {
        //라디오버튼에서 체크된 index가져오기

        val radioButtonCheck = binding.storeHomeReportRadio.checkedRadioButtonId
        if (radioButtonCheck == -1) {
            Toast.makeText(requireContext(), "사유를 선택해 주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val radioButton: View = binding.storeHomeReportRadio.findViewById(radioButtonCheck)
        val index = binding.storeHomeReportRadio.indexOfChild(radioButton)
        if (binding.storeHomeReportContent.text.length < 6) {
            Toast.makeText(requireContext(), "공백을 포함하지 않고 5자 이상 입력해야 합니다", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val gubun1 = "00${index + 1}"
        val contents = binding.storeHomeReportContent.text.toString()
        storeViewModel.storeReport(gubun1, contents)
    }
}