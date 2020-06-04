package com.goodchoice.android.ohneulen.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchRestaurantItemBinding
import com.goodchoice.android.ohneulen.model.Restaurant
import timber.log.Timber

class SearchRestaurantAdapter :
    RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantViewHolder>() {
    var itemList = listOf<Restaurant>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<SearchRestaurantItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_restaurant_item,
            parent,
            false
        ).let {
            SearchRestaurantViewHolder(it)
        }


    override fun onBindViewHolder(holder: SearchRestaurantViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class SearchRestaurantViewHolder(private val binding: SearchRestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurant) {
            binding.apply {
                restaurant = item
                executePendingBindings()
                root.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount() = itemList.size

}