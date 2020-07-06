package com.goodchoice.android.ohneulen.ui.mypage

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageFragmentBinding
import com.goodchoice.android.ohneulen.ui.login.LoginFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker

class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private lateinit var binding: MypageFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun infoClick(view: View) {
        replaceAppbarFragment(MyPageInfoAppBarFragment.newInstance())
        replaceMainFragment(MyPageInfoFragment.newInstance())

    }

    fun nameClick(view: View) {
//        replaceMainFragment(LoginFragment.newInstance())
    }

    fun goodClick(view: View) {
        replaceAppbarFragment(MyPageGoodAppBarFragment.newInstance())
        replaceMainFragment(MyPageGoodFragment.newInstance())
    }

    fun questionsClick(view: View) {

    }

    fun frequentlyQClick(view: View) {
    }


}