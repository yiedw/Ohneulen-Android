package com.goodchoice.android.ohneulen.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.databinding.ImageDetailItemBinding

class ImageDetailAdapter :
    RecyclerView.Adapter<ImageDetailAdapter.StoreImageDetailViewHolder>() {

    var dialogFragment = DialogFragment()
    var imageList = listOf<Image>()

    interface OnNextClickListener {
        fun onNextClick(pos: Int)
    }

    private var mListener: OnNextClickListener? = null

    fun setOnNextClickListener(listener: OnNextClickListener) {
        this.mListener = listener
    }

    inner class StoreImageDetailViewHolder(
        private val binding: ImageDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            binding.apply {
                image = item
//                if (imageList.size - 1 == adapterPosition) {
//                    storeImageDetailRight.visibility = View.GONE
//                } else {
//                    storeImageDetailRight.visibility = View.VISIBLE
//                }
//
//                if (adapterPosition == 0) {
//                    storeImageDetailLeft.visibility = View.GONE
//                } else {
//                    storeImageDetailLeft.visibility = View.VISIBLE
//                }
//                storeImageDetailLeft.setOnClickListener {
//                    mListener!!.onNextClick(adapterPosition - 1)
//                }
//                storeImageDetailRight.setOnClickListener {
//                    mListener!!.onNextClick(adapterPosition + 1)
//                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<ImageDetailItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.image_detail_item,
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