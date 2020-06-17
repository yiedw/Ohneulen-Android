package com.goodchoice.android.ohneulen.ui.partner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerAppbarFragmentBinding
import com.goodchoice.android.ohneulen.ui.home.HomeAppBarFragment
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment

class PartnerAppBarFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerAppBarFragment()
    }

    private lateinit var binding: PartnerAppbarFragmentBinding

    //나중에 되돌리기
    private val initMainFragment: ViewGroup.LayoutParams =
        MainActivity.mainFrameLayout.layoutParams

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.partner_appbar_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }


    fun backClick(view: View) {
        MainActivity.mainFrameLayout.layoutParams=initMainFragment
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance())
    }

    fun shareClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val shareLink = "sample"
        intent.putExtra(Intent.EXTRA_TEXT, shareLink)
        val sharing = Intent.createChooser(intent, "공유하기")
        startActivity(sharing)
    }


}