package com.goodchoice.android.ohneulen.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Inquire
import com.goodchoice.android.ohneulen.databinding.InquireItemBinding
import timber.log.Timber

class InquireAdapter : RecyclerView.Adapter<InquireAdapter.InquireViewHolder>() {
    var inquireList = listOf<Inquire>()

    inner class InquireViewHolder(private val binding: InquireItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Inquire) {
            binding.apply {
                inquireItem.setOnClickListener {
                    if (inquireItemDetail.visibility == View.GONE)
                        inquireItemDetail.visibility = View.VISIBLE
                    else
                        inquireItemDetail.visibility = View.GONE
                }
                inquire = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<InquireItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.inquire_item,
            parent,
            false
        ).let {
            InquireViewHolder(it)
        }

    override fun getItemCount() = inquireList.size

    override fun onBindViewHolder(holder: InquireViewHolder, position: Int) {
        holder.bind(inquireList[position])
    }
}