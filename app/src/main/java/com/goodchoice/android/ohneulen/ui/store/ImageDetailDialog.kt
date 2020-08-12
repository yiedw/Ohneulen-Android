package com.goodchoice.android.ohneulen.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.ImageDetailBinding
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageDetailDialog(private val index: Int) : DialogFragment(), OnBackPressedListener {
    companion object {
        fun newInstance(index: Int) = ImageDetailDialog(index)
    }

    private lateinit var binding: ImageDetailBinding

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
            R.layout.image_detail,
            null,
            false
        )

        binding.lifecycleOwner = binding.lifecycleOwner
        binding.viewModel = storeViewModel
        binding.dialog = this
        
        storeViewModel.storeImageDetailIndex = index
        return binding.root
    }

    fun leftClick(){
//        binding.storeImageDetailRv.adapterp
    }

    override fun onBackPressed() {
        dismiss()
    }


}