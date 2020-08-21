package com.goodchoice.android.ohneulen.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.databinding.ImageDetailReviewBinding
import com.goodchoice.android.ohneulen.databinding.ImageDetailStoreBinding
import com.goodchoice.android.ohneulen.ui.adapter.ImageDetailAdapter
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.OnBackPressedListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageDetailReviewDialog(private val reviewPosition: Int) : DialogFragment(),
    OnBackPressedListener {
    companion object {
        fun newInstance(reviewPosition: Int) =
            ImageDetailReviewDialog(reviewPosition)
    }

    private lateinit var binding: ImageDetailReviewBinding
    private val storeViewModel: StoreViewModel by viewModel()
    private var imagePosition = 0
    private var reviewImageList = listOf<Image>()

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
            R.layout.image_detail_review,
            null,
            false
        )
        reviewImageList = storeViewModel.storeDetail.value!!.reviewList[reviewPosition].imgList
        binding.fragment = this
        adapterSetting()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        storeViewModel.loading.observe(viewLifecycleOwner, Observer {
//            if (it) {
        (binding.imageDetailReviewRv.adapter as ImageDetailAdapter).imagePosition.observe(
            viewLifecycleOwner,
            Observer { pos ->
                imagePosition = pos
                if (pos == 0) {
                    binding.imageDetailReviewLeft.visibility = View.GONE
                } else {
                    binding.imageDetailReviewLeft.visibility = View.VISIBLE
                }
                if (pos == storeViewModel.storeDetail.value!!.reviewList[reviewPosition].imgList.size - 1) {
                    binding.imageDetailReviewRight.visibility = View.GONE
                } else {
                    binding.imageDetailReviewRight.visibility = View.VISIBLE
                }
//                        view.invalidate()
            })
//            }
//        })

    }

    override fun onDestroy() {
        super.onDestroy()
        storeViewModel.loading.postValue(false)
    }

    private fun adapterSetting() {
        val linearLayoutManager = LinearLayoutManager(binding.imageDetailReviewRv.context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.imageDetailReviewRv.layoutManager = linearLayoutManager
        binding.imageDetailReviewRv.onFlingListener = null

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.imageDetailReviewRv)

        val adapter = ImageDetailAdapter().apply {
            imageList = reviewImageList
            binding.imageDetailReviewRv.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
                        val pos: Int = centerView.let {
                            recyclerView.layoutManager!!.getPosition(it!!)
                        }
                        imagePosition.postValue(pos)
                    }
                }
            })
        }
        binding.imageDetailReviewRv.adapter = adapter
    }

    fun onLeftClick(view: View) {
        binding.imageDetailReviewRv.smoothScrollToPosition(imagePosition - 1)
    }

    fun onRightClick(view: View) {
        binding.imageDetailReviewRv.smoothScrollToPosition(imagePosition + 1)
    }

    fun onBackClick(view: View) {
        dismiss()
    }

    override fun onBackPressed() {
        dismiss()
    }


}