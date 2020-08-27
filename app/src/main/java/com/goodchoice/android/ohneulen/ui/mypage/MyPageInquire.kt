package com.goodchoice.android.ohneulen.ui.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.MypageInquireBinding
import com.goodchoice.android.ohneulen.ui.adapter.InquireAdapter
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.util.loginDialog
import com.goodchoice.android.ohneulen.util.popupFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MyPageInquire : Fragment() {

    companion object {
        fun newInstance() = MyPageInquire()
    }

    private lateinit var binding: MypageInquireBinding

    private val mypageViewModel: MyPageViewModel by viewModel()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mypageViewModel.getInquireList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.mypage_inquire,
            container,
            false
        )
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = mypageViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mypageViewModel.mypageInquireList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.mypageInquireRv.visibility = View.GONE
                binding.mypageInquireEmpty.visibility = View.VISIBLE
            } else {
                binding.mypageInquireRv.visibility = View.VISIBLE
                binding.mypageInquireEmpty.visibility = View.GONE
            }

        })

//        if (binding.mypageInquireRv.adapter != null) {
//            binding.mypageInquireRv.adapter!!.registerAdapterDataObserver(object :
//                RecyclerView.AdapterDataObserver() {
//                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                    super.onItemRangeInserted(positionStart, itemCount)
//                    binding.mypageInquireRv.smoothScrollToPosition(0)
//                }
//            })
//        }
        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            if (!it) {
                loginDialog(requireContext(), MyPageInquireAppBar.newInstance(), false)
            }
        })
    }


    fun newClick(view: View) {
        replaceAppbarFragment(MyPageInquireNewAppBar.newInstance())
        popupFragment(MyPageInquireNew.newInstance())
    }

}