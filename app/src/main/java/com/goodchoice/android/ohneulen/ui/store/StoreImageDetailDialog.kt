package com.goodchoice.android.ohneulen.ui.store

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreImageDetailBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreImageDetailDialog(private val index: Int) : DialogFragment(), OnBackPressedListener {
    companion object {
        fun newInstance(index: Int) = StoreImageDetailDialog(index)
    }

    private lateinit var binding: StoreImageDetailBinding

    private val storeViewModel: StoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.image_dialog)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.store_image_detail,
            null,
            false
        )

        binding.lifecycleOwner = binding.lifecycleOwner
        binding.viewModel = storeViewModel
        binding.dialog = this
        storeViewModel.storeImageDetailIndex = index
        return binding.root
    }

    override fun onBackPressed() {
        dismiss()
    }


}