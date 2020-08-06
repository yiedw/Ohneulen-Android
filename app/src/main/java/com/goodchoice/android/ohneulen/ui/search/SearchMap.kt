package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchMapBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.search.*
import net.daum.mf.map.api.MapView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchMap : Fragment() {

    companion object {
        fun newInstance() = SearchMap()
    }

    private val mapView by lazy {
        MapView(requireContext())
    }
    private lateinit var mapViewContainer: ViewGroup
    private val searchViewModel: SearchViewModel by inject()
    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: SearchMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_map,
            container,
            false
        )
        mapView.setZoomLevel(2, false)
        mapViewContainer = binding.searchMapMapView
        addMapView()
//        mapViewContainer.addView(mapView)
        mapView.setOnTouchListener { _, _ -> true }

        //맵 포인트가 바뀌면 바로 반영
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { t ->
                if (TedPermission.isGranted(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOff
                }
                mapView.setMapCenterPoint(t, false)
                searchViewModel.getStoreList()
            }
        )

        //현재위치기반
        if (mainViewModel.searchEditText == ConstList.CURRENT_LOCATION) {
            //퍼미션 리스너 생성
            val permissionListener = object : PermissionListener {
                //승인
                override fun onPermissionGranted() {
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    //거절
                    Toast.makeText(requireContext(), "위치 정보를 확인할수 없습니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            //권한확인
            TedPermission.with(requireContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("위치정보를 확인하기 위해서는 권한이 필요합니다")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check()
        } else {
            //트래킹모드 종료
            if (TedPermission.isGranted(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                mapView.currentLocationTrackingMode =
                    MapView.CurrentLocationTrackingMode.TrackingModeOff
            }
        }
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding.searchMapMapView.removeAllViews()
//    }


    private fun addMapView() {
        binding.searchMapMapView.addView(mapView)
    }
}