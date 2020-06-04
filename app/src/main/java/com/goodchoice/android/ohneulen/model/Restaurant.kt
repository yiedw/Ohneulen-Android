package com.goodchoice.android.ohneulen.model

import androidx.databinding.BindingAdapter

data class Restaurant(
    val code: String,
    val name: String,
    val image: String,
    val rating: String,
    val numberRatings: String,
    val businessTime: String,
    val breakTime: String,
    val new: Boolean
)

fun getRestaurant(): MutableList<Restaurant> {
    val sampleRestaurantList = mutableListOf<Restaurant>()
    for (i in 0..10) {
        sampleRestaurantList.add(
            Restaurant(
                i.toString(), "샘플$i",
                "image", "4.5", "101", "영업시간", "브레이크타임", true
            )
        )
    }
    return sampleRestaurantList
}
