package com.goodchoice.android.ohneulen.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireBinding
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageInquire : Fragment(){

    companion object{
        fun newInstance()=MyPageInquire()
    }

    private lateinit var binding:MypageInquireBinding

    private val mypageViewModel:MyPageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire,
            container,
            false
        )
        binding.fragment=this
        binding.lifecycleOwner=this
        binding.viewModel=mypageViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mypageViewModel.getInquireList()
        mypageViewModel.mypageInquireList.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                binding.mypageInquireRv.visibility=View.GONE
                binding.mypageInquireEmpty.visibility=View.VISIBLE
            }
            else{
                binding.mypageInquireRv.visibility=View.VISIBLE
                binding.mypageInquireEmpty.visibility=View.GONE
            }
        })
        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if(!it){
                loginDialog(requireContext(),MyPageInquireAppBar.newInstance(),false)
            }
        })
    }
    fun newClick(view:View){
        replaceAppbarFragment(MyPageInquireNewAppBar.newInstance())
        popupFragment(MyPageInquireNew.newInstance())
//        replaceMainFragment(MyPageInquireNew.newInstance())
    }

}