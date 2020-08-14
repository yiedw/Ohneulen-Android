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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeReportBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.textColor
import org.koin.android.ext.android.bind
import timber.log.Timber
import java.text.DecimalFormat

class StoreHomeReport(private val storeName: String) : Fragment() {
    companion object {
        fun newInstance(storeName: String) = StoreHomeReport(storeName)
    }

    private lateinit var binding: StoreHomeReportBinding

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
    }

    private fun reportTitle() {
        val textColor = textColor(
            storeName,
            0,
            storeName.length,
            ContextCompat.getColor(requireContext(), R.color.colorOhneulen)
        )
        textColor.setSpan(StyleSpan(Typeface.BOLD),0,storeName.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val mStoreName = TextUtils.concat(
            textColor, "에서 \n정정해야 할 내용이 있나요?"
        )
        binding.storeHomeReportTitle.text = mStoreName

    }


    fun onClick(view: View) {

    }
}