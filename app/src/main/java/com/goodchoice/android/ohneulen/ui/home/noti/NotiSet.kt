package com.goodchoice.android.ohneulen.ui.home.noti

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.NotiSetBinding
import timber.log.Timber

class NotiSet : Fragment() {
    companion object {
        fun newInstance() = NotiSet()
    }

    private lateinit var binding: NotiSetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.noti_set,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    fun noticeSwitch(view: View) {
        if(binding.notiSetNotice.isChecked){
        }
    }
}