package com.goodchoice.android.ohneulen.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.ImageDetailStoreBinding
import com.goodchoice.android.ohneulen.ui.adapter.ImageDetailAdapter
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageDetailStoreDialog(private val index: Int) : DialogFragment(), OnBackPressedListener {
    companion object {
        fun newInstance(index: Int) =
            ImageDetailStoreDialog(index)
    }

    private lateinit var binding: ImageDetailStoreBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private var imagePosition=0

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
            R.layout.image_detail_store,
            null,
            false
        )

        binding.lifecycleOwner = binding.lifecycleOwner
        binding.viewModel = storeViewModel
        binding.dialog = this

        storeViewModel.storeImageDetailIndex = index
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (binding.imageDetailStoreRv.adapter as ImageDetailAdapter).imagePosition.observe(
                    viewLifecycleOwner,
                    Observer { pos ->
                        imagePosition=pos
                        if (pos == 0) {
                            binding.imageDetailStoreLeft.visibility = View.GONE
                        } else {
                            binding.imageDetailStoreLeft.visibility = View.VISIBLE
                        }
                        if (pos == storeViewModel.storeDetail.value!!.storeInfo.image.size - 1) {
                            binding.imageDetailStoreRight.visibility = View.GONE
                        } else {
                            binding.imageDetailStoreRight.visibility = View.VISIBLE
                        }
//                        view.invalidate()
                    })
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        storeViewModel.loading.postValue(false)
    }
    fun onLeftClick(view:View){
        binding.imageDetailStoreRv.smoothScrollToPosition(imagePosition-1)
    }
    fun onRightClick(view:View){
        binding.imageDetailStoreRv.smoothScrollToPosition(imagePosition+1)
    }

    fun onBackClick(view: View) {
        dismiss()
    }

    override fun onBackPressed() {
        dismiss()
    }


}