package com.goodchoice.android.ohneulen.ui.store

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteFragmentBinding
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteImageItemBinding
import com.goodchoice.android.ohneulen.util.dp
import com.goodchoice.android.ohneulen.util.px
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.store_review_write_fragment.view.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class StoreReviewWriteFragment : Fragment() {

    companion object {
        fun newInstance() = StoreReviewWriteFragment()
    }

    private lateinit var binding: StoreReviewWriteFragmentBinding
    private var selectedUriList: List<Uri>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_review_write_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun imageAdd(view: View) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                TedImagePicker.with(requireContext())
                    .errorListener { message -> Timber.e(message) }
                    .selectedUri(selectedUriList)
                    .max(5, "5개만 가능")
                    .startMultiImage { list: List<Uri> -> showMultiImage(list) }

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Timber.e(deniedPermissions.toString())

            }

        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setRationaleMessage("사진을 가져오기 위해서는 갤러리 접근 권한이 필요합니다")
            .setDeniedMessage("거부하면 사진을 가져올수없다")
            .setPermissions(android.Manifest.permission.READ_CONTACTS)
            .check()
    }

    private fun showMultiImage(uriList: List<Uri>) {
        selectedUriList = uriList
        val viewSize = 50.px()
        uriList.forEach {
            val itemBinding =
                StoreReviewWriteImageItemBinding.inflate(LayoutInflater.from(requireContext()))
            val imageBitmap =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        it
                    )
                } else {
                    val imageSource =
                        ImageDecoder.createSource(requireContext().contentResolver, it)
                    ImageDecoder.decodeBitmap(imageSource)
                }

            val file= File(it.path)
            Timber.e(file.length().toString())

            val stream=ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,10,stream)
            Timber.e(stream.toByteArray().size.toString())

            Glide.with(requireContext())
                .load(it)
                .apply(RequestOptions().centerCrop())
                .into(itemBinding.storeReviewWriteImage)
            //jpg 파일 가져오기
            val layoutParams = FrameLayout.LayoutParams(viewSize, viewSize)
            layoutParams.setMargins(5.px(), 5.px(), 5.px(), 5.px())
            itemBinding.root.layoutParams = layoutParams
            binding.storeReviewWriteImage.addView(itemBinding.root)

//            Timber.e(imageBitmap.width.toString())
        }
    }


    fun decodeSampledBitmapFromResource(bitmap:Bitmap,level:Int): Bitmap? {
        val stream=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,level,stream)
        val byteArray=stream.toByteArray()
        val compressedBitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        bitmap.recycle()
        return compressedBitmap
    }
}