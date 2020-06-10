package com.goodchoice.android.ohneulen.ui.partner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerMenuFragmentBinding

class PartnerMenuFragment:Fragment() {
    companion object{
        fun newInstance()=PartnerMenuFragment()
    }

    private lateinit var binding:PartnerMenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.partner_menu_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner=this@PartnerMenuFragment
            viewModel=ViewModelProvider(this@PartnerMenuFragment).get(PartnerViewModel::class.java)
        }
    }
}