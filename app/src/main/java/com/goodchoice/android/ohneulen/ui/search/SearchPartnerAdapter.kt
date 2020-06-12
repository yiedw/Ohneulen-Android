package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchPartnerItemBinding
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.ui.partner.PartnerFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import timber.log.Timber

class SearchPartnerAdapter :
    RecyclerView.Adapter<SearchPartnerAdapter.SearchPartnerViewHolder>() {
    var itemList = listOf<Partner>()

    inner class SearchPartnerViewHolder(private val binding: SearchPartnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Partner) {
            binding.apply {
                partner = item
                executePendingBindings()
                root.setOnClickListener {
                    replaceMainFragment(PartnerFragment.newInstance())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<SearchPartnerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_partner_item,
            parent,
            false
        ).let {
            SearchPartnerViewHolder(it)
        }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: SearchPartnerViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


}