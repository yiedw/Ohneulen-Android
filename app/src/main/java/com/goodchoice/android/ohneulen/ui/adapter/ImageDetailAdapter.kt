package com.goodchoice.android.ohneulen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Image
import com.goodchoice.android.ohneulen.data.model.Photo
import com.goodchoice.android.ohneulen.databinding.ImageDetailItemBinding
import timber.log.Timber


class ImageDetailAdapter :
    RecyclerView.Adapter<ImageDetailAdapter.ImageDetailViewHolder>() {

    var imageList = listOf<Image>()
    var imagePosition= MutableLiveData<Int>(0)

    inner class ImageDetailViewHolder(
        private val binding: ImageDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {
            binding.apply {
                Timber.e(item.photoURL)
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
            ImageDetailViewHolder(it)
        }


    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageDetailViewHolder, position: Int) {
//        imagePosition.postValue(position)
        holder.bind(imageList[position])
    }
}