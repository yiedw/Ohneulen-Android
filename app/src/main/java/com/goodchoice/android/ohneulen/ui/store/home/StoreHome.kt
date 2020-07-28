package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.StoreHomeBinding
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreHome : Fragment() {
    companion object {
        fun newInstance() =
            StoreHome()
    }

    private lateinit var binding: StoreHomeBinding
    private val storeViewModel: StoreViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_home,
            container,
            false
        )
//        binding.storeHome.scrollTo(0, 0)
        binding.fragment = this
        binding.store=storeViewModel.storeInfo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun reportClick(view: View) {
//        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        addMainFragment(StoreHomeReport.newInstance(), true)
    }

}