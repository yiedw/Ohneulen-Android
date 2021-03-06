package com.goodchoice.android.ohneulen.ui.store.review

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteBinding
import com.goodchoice.android.ohneulen.databinding.StoreReviewWriteImageItemBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.dpToPx
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class StoreReviewWrite : Fragment() {

    companion object {
        fun newInstance() =
            StoreReviewWrite()
    }

    private lateinit var binding: StoreReviewWriteBinding
    private var selectedUriList = mutableListOf<Uri>()
    private val storeViewModel: StoreViewModel by viewModel()
    private var mToast: Toast? = null

//    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainActivity.bottomNav.visibility = View.GONE
        storeViewModel.reviewImgList.clear()
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
        binding.viewModel = storeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.storeReviewWriteEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.storeReviewPointLinearLayout.visibility = View.GONE
            } else {
                binding.storeReviewPointLinearLayout.visibility = View.VISIBLE
//                binding.storeReviewWriteScroll.scrollToB
                binding.storeReviewWriteScroll.post {
                    binding.storeReviewWriteScroll.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        }



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

        //리뷰 작성후 성공 실패인지 판단
        storeViewModel.setReviewCode.observe(viewLifecycleOwner, Observer {
            if (it == ConstList.SUCCESS) {
                Toast.makeText(requireContext(), "후기가 등록 되었습니다", Toast.LENGTH_SHORT).show()
                storeViewModel.setReviewCode.postValue("")
                storeViewModel.getStoreDetail(StoreFragment.storeSeq)
                storeViewModel.storeReviewHeightCheck = true
                replaceAppbarFragment(StoreAppBar.newInstance())
                MainActivity.supportFragmentManager.popBackStack()

            } else if (it == ConstList.REQUIRE_LOGIN) {
                loginDialog(requireContext(), false)
            }
        })

        //사진 용량체크
//        storeViewModel.toastMessageCheck.observe(viewLifecycleOwner, Observer {
//            if (it == "321") {
//                Toast.makeText(requireContext(), "사진의 해상도가 너무 높습니다", Toast.LENGTH_SHORT).show()
//            }
//            storeViewModel.toastMessageCheck.postValue("000")
//
//        })
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
        val width = 60.dpToPx()
        val height = 60.dpToPx()
        uriList.forEach {
            val uri = it
            val file = File(uri.path!!)
            //이미지를 비트맵으로 변환시켜서 높낮이, 사이즈 확인
            // height,width<15000, size<20480Kb
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(File(uri.path!!).absolutePath, options)
            val imageHeight = options.outHeight
            val imageWidth = options.outWidth
            val imageSizeKb = file.length() / 1024
            if (imageHeight < 15000 && imageWidth < 15000 && imageSizeKb < 20480) {
//            Timber.e(imageWidth.toString())
//            Timber.e(imageHeight.toString())
//            Timber.e(file.length().to)

                storeViewModel.imageUpload(file)    //서버로 이미지 파일 전송
                val itemBinding =
                    StoreReviewWriteImageItemBinding.inflate(LayoutInflater.from(requireContext()))
                Glide.with(requireContext())
                    .load(it)
                    .apply(RequestOptions().centerCrop())
                    .into(itemBinding.storeReviewWriteImage)
                //선택한 이미지를 넣을 레이아웃을 새로 만듬
                val layoutParams = FrameLayout.LayoutParams(width, height)
                layoutParams.leftMargin = binding.storeReviewWriteImageP.marginLeft
                layoutParams.rightMargin = binding.storeReviewWriteImageP.marginLeft
                itemBinding.root.layoutParams = layoutParams
                itemBinding.root.setOnClickListener {
                    binding.storeReviewWriteImage.removeView(itemBinding.root)
                    selectedUriList.remove(uri)
                }
                binding.storeReviewWriteImage.addView(itemBinding.root, 0)
//            storeViewModel.imageUpload(data)
            } else {
                Toast.makeText(requireContext(), "용량이 큰 파일 발견", Toast.LENGTH_SHORT).show()
            }
        }
        storeViewModel.reviewImgList.clear()
    }

    private fun submitCheck(): Boolean {
        val reviewSelect01 =
            (binding.storeReviewRg1.indexOfChild(binding.storeReviewRg1.findViewById(binding.storeReviewRg1.checkedRadioButtonId)) + 1).toString()
        val reviewSelect02 =
            (binding.storeReviewRg2.indexOfChild(binding.storeReviewRg2.findViewById(binding.storeReviewRg2.checkedRadioButtonId)) + 1).toString()
        val reviewSelect03 =
            (binding.storeReviewRg3.indexOfChild(binding.storeReviewRg3.findViewById(binding.storeReviewRg3.checkedRadioButtonId)) + 1).toString()
        val reviewSelect04 =
            (binding.storeReviewRg4.indexOfChild(binding.storeReviewRg4.findViewById(binding.storeReviewRg4.checkedRadioButtonId)) + 1).toString()
        val reviewSelect05 =
            (binding.storeReviewRg5.indexOfChild(binding.storeReviewRg5.findViewById(binding.storeReviewRg5.checkedRadioButtonId)) + 1).toString()

        if (binding.storeReviewWriteRating.rating.toInt() == 0) {
            showToast("평점을 입력해 주세요")
            return false
        } else if (reviewSelect01 == "0") {
            showToast("맛을 평가해 주세요")
            return false
        } else if (reviewSelect02 == "0") {
            showToast("가격을 평가해 주세요")
            return false
        } else if (reviewSelect03 == "0") {
            showToast("직원 친절도를 평가해 주세요")
            return false
        } else if (reviewSelect04 == "0") {
            showToast("분위기를 평가해 주세요")
            return false
        } else if (reviewSelect05 == "0") {
            showToast("청결도를 평가해 주세요")
            return false
        } else if (binding.storeReviewWriteEt.length() < 5) {
            showToast("후기를 5자 이상 작성해 주세요")
            return false
        }
        return true
    }

    @SuppressLint("ShowToast")
    private fun showToast(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(message)
        }
        mToast!!.show()
    }


    fun onSubmitClick(view: View) {
        val point0 = binding.storeReviewWriteRating.rating.toInt().toString()
        val reviewSelect01 =
            (5 - binding.storeReviewRg1.indexOfChild(binding.storeReviewRg1.findViewById(binding.storeReviewRg1.checkedRadioButtonId))).toString()
        val reviewSelect02 =
            (5 - binding.storeReviewRg2.indexOfChild(binding.storeReviewRg2.findViewById(binding.storeReviewRg2.checkedRadioButtonId))).toString()
        val reviewSelect03 =
            (5 - binding.storeReviewRg3.indexOfChild(binding.storeReviewRg3.findViewById(binding.storeReviewRg3.checkedRadioButtonId))).toString()
        val reviewSelect04 =
            (5 - binding.storeReviewRg4.indexOfChild(binding.storeReviewRg4.findViewById(binding.storeReviewRg4.checkedRadioButtonId))).toString()
        val reviewSelect05 =
            (5 - binding.storeReviewRg5.indexOfChild(binding.storeReviewRg5.findViewById(binding.storeReviewRg5.checkedRadioButtonId))).toString()
        val reviewText = binding.storeReviewWriteEt.text.toString()
        val reviewImgList = mutableListOf<ByteArray>()
        for (i in selectedUriList) {
            requireContext().contentResolver.openInputStream(i)!!.buffered().use {
                reviewImgList.add(it.readBytes())

//            val inputStream=requireContext().contentResolver.openInputStream(i)!!
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                val baos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                val data = baos.toByteArray()
//                reviewImgList.add(data)
            }
        }
        if (submitCheck()) {
            storeViewModel.setReview(
                point0,
                reviewSelect01,
                reviewSelect02,
                reviewSelect03,
                reviewSelect04,
                reviewSelect05,
                reviewText
//                reviewImgList
            )
        }
    }

    fun uri2File(uri: Uri) {
        val file = File(uri.path!!)
    }


}