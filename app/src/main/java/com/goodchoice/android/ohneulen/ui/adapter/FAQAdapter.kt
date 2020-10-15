package com.goodchoice.android.ohneulen.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.FAQ
import com.goodchoice.android.ohneulen.databinding.FaqItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.mypage.MyPageViewModel
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import timber.log.Timber

class FAQAdapter : ListAdapter<FAQ, FAQAdapter.FAQViewHolder>(FAQDiffUtil) {

    lateinit var myPageViewModel: MyPageViewModel

    inner class FAQViewHolder(private val binding: FaqItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: FAQ) {
            binding.apply {
                faq = item
                if (item.check) {
                    //클릭했을때
                    faqItemContent.visibility = View.VISIBLE
                    Glide.with(root.context).load(R.drawable.faq_close).into(faqItemMore)
                } else {
                    //기본상태
                    faqItemContent.visibility = View.GONE
                    Glide.with(root.context).load(R.drawable.faq_more).into(faqItemMore)
                }
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

    //    setHasStableIds(true) 사용하기위해 씀
    override fun getItemId(position: Int): Long {
        return getItem(position).seq.toLong()
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