package com.goodchoice.android.ohneulen.ui.partner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerMenuFragmentBinding
import com.goodchoice.android.ohneulen.databinding.PartnerMenuItemBinding
import com.goodchoice.android.ohneulen.model.PartnerMenu
import timber.log.Timber

class PartnerMenuAdapter : RecyclerView.Adapter<PartnerMenuAdapter.PartnerMenuViewHolder>() {
    var itemList = listOf<PartnerMenu>()

    override fun getItemCount() = itemList.size

    inner class PartnerMenuViewHolder(private val binding: PartnerMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(item:PartnerMenu){
            binding.apply {
               partnerMenu=item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<PartnerMenuItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.partner_menu_item,
            parent,
            false

        ).let {
            PartnerMenuViewHolder(it)
        }

    override fun onBindViewHolder(holder: PartnerMenuViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}

