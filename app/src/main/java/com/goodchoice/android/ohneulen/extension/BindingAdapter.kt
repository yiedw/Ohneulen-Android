package com.goodchoice.android.ohneulen.extension

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.model.Restaurant
import com.goodchoice.android.ohneulen.ui.search.SearchRestaurantAdapter


@BindingAdapter("restaurant")
fun setSearchRestaurant(recyclerView: RecyclerView, items: List<Restaurant>?) {
    recyclerView.adapter = SearchRestaurantAdapter().apply {
        itemList = items ?: emptyList()
        notifyDataSetChanged()
    }
}
