package com.goodchoice.android.ohneulen.ui.dialog

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.ImageDetailReviewBinding
import com.goodchoice.android.ohneulen.databinding.LoadingBinding

class LoadingDialog(private val text: String) : DialogFragment() {
    companion object {
        fun newInstance(text: String) = LoadingDialog(text)
    }

    private lateinit var binding: LoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Transparent_Permission)
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
            R.layout.loading,
            null,
            false
        )
        binding.loadingText.text = text
//        val animation=binding.loadingGif.background as AnimationDrawable
//        binding.loadingGif.post {
//            animation.start()
//
//        }
        Glide.with(requireContext()).load(R.raw.loading_test)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.loadingGif)
        return binding.root

    }

}