package com.goodchoice.android.ohneulen.ui.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.StoreAppbarFragmentBinding

class StoreAppBarFragment : Fragment() {

    companion object {
        fun newInstance() = StoreAppBarFragment()
    }

    private lateinit var binding: StoreAppbarFragmentBinding

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
            R.layout.store_appbar_fragment,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }


    fun backClick(view: View) {
        MainActivity.supportFragmentManager.popBackStack()
        MainActivity.supportFragmentManager.popBackStack()
//        MainActivity.searchMapView.postValue(true)
        MainActivity.mainFrameLayout.layoutParams=initMainFragment
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