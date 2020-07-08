package com.goodchoice.android.ohneulen.ui.store.review

import android.net.Uri
import android.os.Bundle
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
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import timber.log.Timber

class StoreReviewWriteFragment : Fragment() {

    companion object {
        fun newInstance() =
            StoreReviewWriteFragment()
    }

    private lateinit var binding: StoreReviewWriteFragmentBinding
    private var  selectedUriList:List<Uri>?=null

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
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun imageAdd(view:View){
        val permissionListener=object :PermissionListener{
            override fun onPermissionGranted() {
               TedImagePicker.with(requireContext())
                   .errorListener { message -> Timber.e(message) }
                   .selectedUri(selectedUriList)
                   .max(10,"10개만 가능")
                   .startMultiImage { list:List<Uri> -> showMultiImage(list) }

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

    private fun showMultiImage(uriList:List<Uri>){
        selectedUriList=uriList
        val viewSize=50.dp()
        uriList.forEach {
            val itemBinding= StoreReviewWriteImageItemBinding.inflate(LayoutInflater.from(requireContext()))
            Glide.with(requireContext())
                .load(it)
                .apply(RequestOptions().centerCrop())
                .into(itemBinding.storeReviewWriteImage)
            val layoutParams=FrameLayout.LayoutParams(viewSize,viewSize)
            layoutParams.setMargins(5.dp(),5.dp(),5.dp(),5.dp())
            itemBinding.root.layoutParams=layoutParams
            binding.storeReviewWriteImage.addView(itemBinding.root)
        }
    }
    fun onClick(view:View){

    }
}