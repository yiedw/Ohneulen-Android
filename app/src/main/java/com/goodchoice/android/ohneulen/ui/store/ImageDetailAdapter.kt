package com.goodchoice.android.ohneulen.ui.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.databinding.ImageDetailItemBinding
import timber.log.Timber

class ImageDetailAdapter :
    RecyclerView.Adapter<ImageDetailAdapter.StoreImageDetailViewHolder>() {

    var dialogFragment = DialogFragment()
    var imageList = listOf<Image>()
    var imagePosition=MutableLiveData<Int>(0)

//    interface OnRightClickListener {
//        fun onNextClick(pos: Int)
//    }
//    interface OnLeftClickListener {
//        fun onLeftClick(pos: Int)
//    }
//
//    private var mListener: OnRightClickListener? = null
//
//    fun setOnNextClickListener(listener: OnRightClickListener) {
//        this.mListener = listener
//    }

    inner class StoreImageDetailViewHolder(
        private val binding: ImageDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            binding.apply {
                image = item


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
//        imagePosition.postValue(position)
        holder.bind(imageList[position])
    }
}