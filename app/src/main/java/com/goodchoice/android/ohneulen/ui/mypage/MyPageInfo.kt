package com.goodchoice.android.ohneulen.ui.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInfoBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker

class MyPageInfo : Fragment() {
    companion object {
        fun newInstance() = MyPageInfo()
    }

    private lateinit var binding: MypageInfoBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_info,
            container,
            false
        )
        binding.fragment=this
        binding.mypageInfo.setOnTouchListener(object : OnSwipeGesture(requireContext()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                MainActivity.supportFragmentManager.popBackStack()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //바텀바 숨기기
        val animation = AlphaAnimation(0f, 1f)
        MainActivity.bottomNav.visibility = View.GONE
        MainActivity.bottomNav.animation = animation
    }

    fun imageClick(view: View) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                TedImagePicker.with(requireContext())
                    .start { uri -> showImage(uri) }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            }


        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한이 필요")
            .setPermissions(Manifest.permission.READ_CONTACTS)
            .check()
    }

    fun withdrawalClick(view:View){
        replaceAppbarFragment(MyPageSecessionAppBar.newInstance())
        replaceMainFragment(MyPageSecession.newInstance())
    }

    private fun showImage(uri: Uri) {
        binding.mypageInfoImage.clipToOutline = true
        Glide.with(requireContext())
            .load(uri)
            .apply(RequestOptions().centerCrop())
            .into(binding.mypageInfoImage)
    }
}