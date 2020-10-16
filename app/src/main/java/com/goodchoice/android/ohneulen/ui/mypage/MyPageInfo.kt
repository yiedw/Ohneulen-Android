package com.goodchoice.android.ohneulen.ui.mypage

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInfoBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.OnSwipeGesture
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.goodchoice.android.ohneulen.util.pwCheck
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.android.ext.android.inject
import javax.inject.Inject

class MyPageInfo : Fragment() {
    companion object {
        fun newInstance() = MyPageInfo()
    }

    private lateinit var binding: MypageInfoBinding
    private val loginViewModel: LoginViewModel by inject()
    private val mypageViewModel: MyPageViewModel by inject()

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
        binding.fragment = this
        binding.viewModel = loginViewModel
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

        //회원탈퇴 밑줄긋기
        binding.mypageInfoWithdrawal.paintFlags =
            binding.mypageInfoWithdrawal.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //멤버업데이트 리스폰스를 옵저빙
        mypageViewModel.memberUpdateResponse.observe(viewLifecycleOwner, Observer {
            if (it.resultCode == ConstList.SUCCESS) {
                Toast.makeText(requireContext(), "변경사항이 저장되었습니다", Toast.LENGTH_SHORT).show()
                //비밀번호 지워주기
                binding.mypageInfoOldPw.text.clear()
                binding.mypageInfoNewPw.text.clear()
                binding.mypageInfoRePw.text.clear()

            } else if (it.resultCode == ConstList.PASSWORD_CHANGE_FAIL) {
                Toast.makeText(requireContext(), "비밀번호 재설정에 실패 하였습니다", Toast.LENGTH_SHORT).show()
            }
            //초기화
            mypageViewModel.memberUpdateResponse.value!!.resultCode = ""
        })
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
            .setDeniedMessage("사진을 가져오기 위해서는 권한이 필요합니다")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun showImage(uri: Uri) {
        binding.mypageInfoImage.clipToOutline = true
        Glide.with(requireContext())
            .load(uri)
            .apply(RequestOptions().centerCrop())
            .into(binding.mypageInfoImage)
    }

    fun withdrawalClick(view: View) {
//        replaceAppbarFragment(MyPageWithdrawalAppBar.newInstance())
//        replaceMainFragment(MyPageWithdrawal.newInstance())
    }

    fun submitClick(view: View) {
        val oldPw = binding.mypageInfoOldPw.text.toString()
        val newPw = binding.mypageInfoNewPw.text.toString()
        val rePw = binding.mypageInfoRePw.text.toString()
        val nickName = binding.mypageInfoNickName.text.toString()

        val toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)

        //비밀번호 변경하기전에 체크
        if (!pwCheck(newPw)) {
            toast.setText("비밀번호는 영문,숫자로 조합된 8자리 이상으로 입력하세요")
            toast.show()
            return
        } else if (newPw != rePw) {
            toast.setText("입력한 비밀번호가 서로 맞지 않습니다.")
            toast.show()
            return
        } else if (newPw == oldPw) {
            toast.setText("기존에 사용하던 비밀번호와 동일한 비밀번호가 사용되었습니다.")
            toast.show()
            return
        }
        //멤버 정보 업데이트
        mypageViewModel.memberUpdate(oldPw, newPw, rePw, nickName)

    }

}