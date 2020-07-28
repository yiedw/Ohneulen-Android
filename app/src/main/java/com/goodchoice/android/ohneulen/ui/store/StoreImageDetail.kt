package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreImageDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreImageDetail(private val index: Int) : Fragment() {
    companion object {
        fun newInstance(index: Int) = StoreImageDetail(index)
    }

    private lateinit var binding: StoreImageDetailBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_image_detail,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = storeViewModel
        storeViewModel.storeImageDetailIndex=index
        return binding.root
    }
}