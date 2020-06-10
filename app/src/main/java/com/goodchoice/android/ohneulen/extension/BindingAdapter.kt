package com.goodchoice.android.ohneulen.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.model.Partner
import com.goodchoice.android.ohneulen.model.PartnerMenu
import com.goodchoice.android.ohneulen.ui.partner.PartnerMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchPartnerAdapter


@BindingAdapter("partner")
fun setSearchPartner(recyclerView: RecyclerView, items: List<Partner>?) {
    recyclerView.adapter = SearchPartnerAdapter().apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("partnerMenu")
fun setPartnerMenu(recyclerView: RecyclerView,items:List<PartnerMenu>?){
    recyclerView.adapter=PartnerMenuAdapter().apply {
        itemList=items ?: emptyList()
        notifyDataSetChanged()
    }
}

