package com.goodchoice.android.ohneulen.ui.store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.ImageDetailStoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreImageDetail(private val index: Int) : Fragment() {
    companion object {
        fun newInstance(index: Int) = StoreImageDetail(index)
    }

    private lateinit var binding: ImageDetailStoreBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.image_detail_store,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = storeViewModel
        storeViewModel.storeImageDetailIndex = index
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}