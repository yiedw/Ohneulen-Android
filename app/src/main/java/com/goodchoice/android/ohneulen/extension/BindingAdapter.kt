package com.goodchoice.android.ohneulen.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.model.Store
import com.goodchoice.android.ohneulen.model.PartnerMenu
import com.goodchoice.android.ohneulen.ui.partner.PartnerMenuAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchFilterAdapter
import com.goodchoice.android.ohneulen.ui.search.SearchPartnerAdapter
import com.goodchoice.android.ohneulen.util.ConstList


@BindingAdapter("store")
fun setSearchStore(recyclerView: RecyclerView, items: List<Store>?) {
    recyclerView.adapter = (recyclerView.adapter as SearchPartnerAdapter).apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("storeAdapter")
fun setStoreAdapter(recyclerView: RecyclerView, adapter: SearchPartnerAdapter?) {
    recyclerView.adapter = adapter
}

@BindingAdapter("partnerMenu")
fun setPartnerMenu(recyclerView: RecyclerView, items: List<PartnerMenu>?) {
    recyclerView.adapter = PartnerMenuAdapter().apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}

@BindingAdapter("mainCategory")
fun setMainCategory(recyclerView: RecyclerView, items: List<String>) {
    recyclerView.adapter = SearchFilterAdapter(ConstList.MAIN_CATEGORY).apply {
        itemList = items
        notifyDataSetChanged()

    }
}

@BindingAdapter("subCategory")
fun setSubCategory(recyclerView: RecyclerView, items: List<String>) {
    recyclerView.adapter = SearchFilterAdapter(ConstList.SUB_CATEGORY).apply {
        itemList = items
        notifyDataSetChanged()
    }
}



