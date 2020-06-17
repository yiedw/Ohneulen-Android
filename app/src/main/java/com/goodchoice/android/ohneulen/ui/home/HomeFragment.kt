package com.goodchoice.android.ohneulen.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.MainViewModel
import com.goodchoice.android.ohneulen.databinding.HomeFragmentBinding
import com.goodchoice.android.ohneulen.ui.search.SearchAppBarFragment
import com.goodchoice.android.ohneulen.ui.search.SearchFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.MapPoint
import timber.log.Timber

class HomeFragment() : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
        var currentLocation=false
    }

//    private lateinit var mainViewModel:MainViewModel
    private lateinit var binding: HomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )
        binding.fragment = this

//        mainViewModel=ViewModelProvider(requireActivity())
//            .get(MainViewModel::class.java)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun searchOnClick(view: View) {
        currentLocation=false
        MainViewModel.liveSearchResult= MutableLiveData(binding.homeEditText.text.toString())
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance())
    }

    fun currentLocationClick(view: View) {
        currentLocation=true
        MainViewModel.liveSearchResult= MutableLiveData("")
        replaceAppbarFragment(SearchAppBarFragment.newInstance())
        replaceMainFragment(SearchFragment.newInstance())
    }


}
