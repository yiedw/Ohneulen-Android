package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.MainViewModel
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFragmentBinding
import com.goodchoice.android.ohneulen.ui.home.HomeFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.search_fragment.*
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.security.Provider

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var switchOn = false
    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var mainViewModel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.search_fragment,
                container,
                false
            )
        binding.fragment = this
        mainViewModel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //다음지도 불러오기
        //try catch 추후 삭제
        try {
            val mapView = mapInit(MapView(requireContext()))
            val mapViewContainer: ViewGroup = search_mapView as ViewGroup
            mapViewContainer.addView(mapView)
        } catch (e: UnsatisfiedLinkError) {
            Timber.e(e)
        } catch (e: NoClassDefFoundError) {
            Timber.e(e)
        }

        //바인딩
        binding.apply {
            lifecycleOwner = this@SearchFragment
            viewModel = searchViewModel
            mainViewModel=mainViewModel

        }


    }

    private fun mapInit(mapView: MapView): MapView {
        mapView.setZoomLevel(2, false)
        if (HomeFragment.currentLocation) {
            val permissionListener=object :PermissionListener{
                override fun onPermissionGranted() {
                    mapView.currentLocationTrackingMode=MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(requireContext(),"위치 정보를 확인할수 없습니다",Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.with(requireContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("위치정보를 확인하기 위해서는 권한이 필요합니다")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
        }
        else{
            mapView.currentLocationTrackingMode=MapView.CurrentLocationTrackingMode.TrackingModeOff

        }

        return mapView
    }

    fun switchClick(view: View) {
        if (!switchOn)
            binding.searchMapView.visibility = View.GONE
        else
            binding.searchMapView.visibility = View.VISIBLE

        switchOn = !switchOn
    }

    fun filterClick(view: View) {
        replaceAppbarFragment(SearchFilterAppbarFragment.newInstance())
        replaceMainFragment(SearchFilterFragment.newInstance())
    }


}