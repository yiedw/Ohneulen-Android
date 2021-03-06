package com.goodchoice.android.ohneulen.ui.store.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMenuBinding
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreMenu : Fragment() {
    companion object {
        fun newInstance() =
            StoreMenu()
    }

    private lateinit var binding: StoreMenuBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_menu,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@StoreMenu
            viewModel=storeViewModel
        }
    }
}