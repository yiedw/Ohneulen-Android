package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeReportFragmentBinding
import com.goodchoice.android.ohneulen.ui.MainActivity

class StoreHomeReportFragment : Fragment() {
    companion object {
        fun newInstance() = StoreHomeReportFragment()
    }

    private lateinit var binding: StoreHomeReportFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home_report_fragment,
            container,
            false
        )
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storeHomeReport.translationY=MainActivity.appbarFrameLayout.height.toFloat()
    }

    fun onClick(view: View) {

    }
}