package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMenuFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreMenuFragment : Fragment() {
    companion object {
        fun newInstance() = StoreMenuFragment()
    }

    private lateinit var binding: StoreMenuFragmentBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_menu_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@StoreMenuFragment
            viewModel = storeViewModel
        }
    }
}