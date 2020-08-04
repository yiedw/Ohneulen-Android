package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.StoreDetail
import com.goodchoice.android.ohneulen.databinding.StoreHomeBinding
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
        binding.fragment = this
        binding.viewModel = storeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.storeDetail.observe(viewLifecycleOwner, Observer {
            storeUpdate(it)
        })

    }

    //개업일 업데이트
    private fun storeUpdate(storeDetail: StoreDetail) {
        val store = storeDetail.storeInfo.store
        val openDate = store.openDate.substring(0, 4) + "." + store.openDate.substring(
            4,
            6
        ) + "." + store.openDate.substring(6)
        val modifyDate = store.modifyDate.substring(0, 4) + "." + store.modifyDate.substring(
            5,
            7
        ) + "." + store.modifyDate.substring(8, 10)
        binding.storeHomeOpenTv2.text = "$openDate\n$modifyDate"
    }

    fun reportClick(view: View) {
//        MainActivity.mainFrameLayout.layoutParams = MainActivity.initMainFrameLayout
        replaceAppbarFragment(StoreHomeReportAppBar.newInstance())
        addMainFragment(
            StoreHomeReport.newInstance(storeViewModel.storeDetail.value!!.storeInfo.store.storeName),
            true
        )
    }

}