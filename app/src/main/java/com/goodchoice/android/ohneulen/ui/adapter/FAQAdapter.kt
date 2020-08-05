package com.goodchoice.android.ohneulen.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.FAQ
import com.goodchoice.android.ohneulen.databinding.FaqItemBinding
import com.goodchoice.android.ohneulen.ui.mypage.MyPageViewModel
import timber.log.Timber

class FAQAdapter : ListAdapter<FAQ, FAQAdapter.FAQViewHolder>(FAQDiffUtil) {

    lateinit var myPageViewModel: MyPageViewModel

    inner class FAQViewHolder(private val binding: FaqItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FAQ) {
            binding.apply {
                if (item.check) {
                    faqItemContent.visibility = View.VISIBLE
//                    Glide.with(binding.root).load(R.drawable.close).into(binding.faqItemOpen)
                } else {
                    faqItemContent.visibility = View.GONE
//                    Glide.with(binding.root).load(R.drawable.open).into(binding.faqItemOpen)
                }
                faqItem.setOnClickListener {
                    for (i in myPageViewModel.mypageFAQList.value!!.indices) {
                        if (i != adapterPosition && myPageViewModel.mypageFAQList.value!![i].check) {
                            myPageViewModel.mypageFAQList.value!![i].check = false
                            notifyItemChanged(i)
                        }
                    }
                    myPageViewModel.mypageFAQList.value!![adapterPosition].check =
                        !myPageViewModel.mypageFAQList.value!![adapterPosition].check
                    notifyItemChanged(adapterPosition)
//                    if (faqItemContent.visibility == View.GONE) {
//                        faqItemContent.visibility = View.VISIBLE
//                        Glide.with(binding.root).load(R.drawable.close).into(binding.faqItemOpen)
//                    } else {
//                        faqItemContent.visibility = View.GONE
//                        Glide.with(binding.root).load(R.drawable.open).into(binding.faqItemOpen)
//                    }
                }
                faq = item
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<FaqItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.faq_item,
            parent,
            false
        ).let {
            FAQViewHolder(it)
        }

    override fun getItemCount() = super.getItemCount()

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object FAQDiffUtil : DiffUtil.ItemCallback<FAQ>() {
    override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem.seq == newItem.seq
    }

    override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        return oldItem == newItem
    }

}