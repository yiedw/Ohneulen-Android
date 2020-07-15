package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeFragmentBinding
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment

class StoreHome : Fragment() {
    companion object {
        fun newInstance() =
            StoreHome()
    }

    private lateinit var binding: StoreHomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home_fragment,
            container,
            false
        )
        binding.storeHome.scrollTo(0, 0)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun submitClick(view: View) {
//        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        addMainFragment(StoreHomeReport.newInstance(), true)
    }

}