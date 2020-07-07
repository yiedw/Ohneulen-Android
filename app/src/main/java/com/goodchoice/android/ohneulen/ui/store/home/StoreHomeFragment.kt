package com.goodchoice.android.ohneulen.ui.store.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreHomeFragmentBinding

class StoreHomeFragment : Fragment() {
    companion object {
        fun newInstance() =
            StoreHomeFragment()
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
        binding.storeHome.scrollTo(0,0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}