package com.goodchoice.android.ohneulen.ui.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.databinding.StoreImageDetailItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import timber.log.Timber

class StoreImageDetailAdapter :
    RecyclerView.Adapter<StoreImageDetailAdapter.StoreImageDetailViewHolder>() {


    var imageList = listOf<Image>()

    interface OnNextClickListener {
        fun onNextClick(pos: Int)
    }

    private var mListener: OnNextClickListener? = null

    fun setOnNextClickListener(listener: OnNextClickListener) {
        this.mListener = listener
    }

    inner class StoreImageDetailViewHolder(
        private val binding: StoreImageDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            binding.apply {
                image = item
                if (imageList.size - 1 == adapterPosition) {
                    storeImageDetailRight.visibility = View.GONE
                } else {
                    storeImageDetailRight.visibility = View.VISIBLE
                }

                if (adapterPosition == 0) {
                    storeImageDetailLeft.visibility = View.GONE
                } else {
                    storeImageDetailLeft.visibility = View.VISIBLE
                }

                storeImageDetailLeft.setOnClickListener {
                    mListener!!.onNextClick(adapterPosition - 1)
                }
                storeImageDetailRight.setOnClickListener {
                    mListener!!.onNextClick(adapterPosition + 1)
                }
                storeImageDetailBack.setOnClickListener {
                    MainActivity.supportFragmentManager.popBackStack()
                    MainActivity.appbarFrameLayout.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<StoreImageDetailItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.store_image_detail_item,
            parent,
            false
        ).let {
            StoreImageDetailViewHolder(it)
        }


    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: StoreImageDetailViewHolder, position: Int) {
        holder.bind(imageList[position])
    }
}