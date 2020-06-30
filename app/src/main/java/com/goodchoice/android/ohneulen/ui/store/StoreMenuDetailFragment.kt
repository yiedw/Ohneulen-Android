package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreMenuDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreMenuDetailFragment : Fragment() {

    companion object {
        fun newInstance() = StoreMenuDetailFragment()
    }

    private lateinit var binding: StoreMenuDetailBinding
    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_menu_detail,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@StoreMenuDetailFragment
            viewModel=storeViewModel
        }
    }


}