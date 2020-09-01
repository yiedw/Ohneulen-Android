package com.goodchoice.android.ohneulen.data.model

data class ImageUpload(
    val client_name: String,
    val file_ext: String,
    val file_name: String,
    val file_path: String,
    val file_size: Double,
    val file_type: String,
    val full_path: String,
    val image_height: Int,
    val image_size_str: String,
    val image_type: String,
    val image_width: Int,
    val is_image: Boolean,
    val orig_name: String,
    val raw_name: String
)