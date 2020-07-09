package com.goodchoice.android.ohneulen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.FAQ
import com.goodchoice.android.ohneulen.databinding.FaqItemBinding

class FAQAdapter :RecyclerView.Adapter<FAQAdapter.FAQViewHolder>(){

    var FAQList= listOf<FAQ>()
    inner class FAQViewHolder(private val binding: FaqItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FAQ) {
            binding.apply {
                faqItem.setOnClickListener {
                    if (faqItemContent.visibility == View.GONE) {
                        faqItemContent.visibility = View.VISIBLE
                        faqItemOpen.text = "close"
                    } else {
                        faqItemContent.visibility = View.GONE
                        faqItemOpen.text = "open"
                    }
                }
                faq=item
                executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        DataBindingUtil.inflate<FaqItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.faq_item,
            parent,
            false
        ).let {
            FAQViewHolder(it)
        }

    override fun getItemCount()=FAQList.size

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.bind(FAQList[position])
    }
}