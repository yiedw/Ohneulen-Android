package com.goodchoice.android.ohneulen.ui.store.review

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteBinding
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteImageItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.KeyboardVisibilityUtils
import com.goodchoice.android.ohneulen.util.dp
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StoreReviewWrite : Fragment() {

    companion object {
        fun newInstance() =
            StoreReviewWrite()
    }

    private lateinit var binding: StoreReviewWriteBinding
    private var selectedUriList: MutableList<Uri>? = null
    private val storeViewMode: StoreViewModel by viewModel()

//    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.store_review_write,
            container,
            false
        )
        binding.fragment = this
        binding.viewModel = storeViewMode

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        storeReviewWriteEt 누르면 화면 맨 위로올리기
//        binding.storeReviewWriteEt.setOnClickListener {
//            binding.storeReviewWriteScroll.post {
//                binding.storeReviewWriteScroll.fullScroll(View.FOCUS_DOWN)
//            }
//        }
        binding.storeReviewWriteEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.storeReviewPointLinearLayout.visibility = View.GONE
            } else {
                binding.storeReviewPointLinearLayout.visibility = View.VISIBLE
            }
        }
//        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
//            onShowKeyboard = { keyboardHeight ->
//                val param=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,keyboardHeight.dp())
//                param.topToBottom=binding.storeReviewWriteBorder.id
//                binding.storeReviewWriteKeyboard.layoutParams=param
//                binding.storeReviewWriteKeyboard.visibility = View.VISIBLE
//                binding.storeReviewWriteScroll.run {
//                    smoothScrollBy(scrollX, scrollY + keyboardHeight)
//                }
//            })


        binding.storeReviewWriteEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.storeReviewWriteEtLength.text =
                    binding.storeReviewWriteEt.text.toString().length.toString()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.bottomNav.visibility = View.VISIBLE
//        keyboardVisibilityUtils.detachKeyboardListeners()
    }

    fun imageAdd(view: View) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                TedImagePicker.with(requireContext())
                    .errorListener { message -> Timber.e(message) }
                    .selectedUri(selectedUriList)
                    .max(5, "최대 5개까지 등록 가능합니다")
                    .startMultiImage { list: List<Uri> -> showMultiImage(list) }

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Timber.e(deniedPermissions.toString())

            }

        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setRationaleMessage("사진을 가져오기 위해서는 갤러리 접근 권한이 필요합니다")
            .setDeniedMessage("권한이 없으면 사진을 가져올 수 없습니다")
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun showMultiImage(uriList: List<Uri>) {
        selectedUriList = uriList.toMutableList()
        val width = 60.dp()
        val height = 60.dp()
        uriList.forEach {
            val uri = it
            val itemBinding =
                StoreReviewWriteImageItemBinding.inflate(LayoutInflater.from(requireContext()))
            Glide.with(requireContext())
                .load(it)
                .apply(RequestOptions().centerCrop())
                .into(itemBinding.storeReviewWriteImage)
            val layoutParams = FrameLayout.LayoutParams(width, height)
            layoutParams.leftMargin = binding.storeReviewWriteImageP.marginLeft
            layoutParams.rightMargin = binding.storeReviewWriteImageP.marginLeft
            itemBinding.root.layoutParams = layoutParams
            itemBinding.root.setOnClickListener {
                binding.storeReviewWriteImage.removeView(itemBinding.root)
                selectedUriList!!.remove(uri)
            }
            binding.storeReviewWriteImage.addView(itemBinding.root, 0)
        }
    }


    fun onClick(view: View) {

    }
}