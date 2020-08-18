package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.SearchMapBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.util.constant.ConstList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchMap : Fragment(), MapView.CurrentLocationEventListener {

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
        mapView.setZoomLevel(3, false)
        mapViewContainer = binding.searchMapMapView
        addMapView()
//        mapViewContainer.addView(mapView)
//        Timber.e(mapView.clipBounds.toString())
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.setOnTouchListener { _, _ -> true }

        mapView.setCurrentLocationEventListener(this)


        //맵 포인트가 바뀌면 바로 반영
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { it ->
                if (TedPermission.isGranted(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
//                    mapView.currentLocationTrackingMode =
//                        MapView.CurrentLocationTrackingMode.TrackingModeOff
                }
                mapView.setMapCenterPoint(it, false)
                circleSearch(it)
            }
        )
        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {
            for (i in it) {
                addMarker(i)
            }

        })


        //현재위치기반
        if (mainViewModel.searchEditText == ConstList.CURRENT_LOCATION) {
            //퍼미션 리스너 생성
            val permissionListener = object : PermissionListener {
                //승인
                override fun onPermissionGranted() {
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                    Timber.e("ASdfsdafsadf321")
//                    val mapPoint:MapPoint=mapView.mapCenterPoint
//                    searchViewModel.kakaoMapPoint.postValue(mapPoint)
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

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding.searchMapMapView.removeAllViews()
//    }


    private fun addMapView() {
        binding.searchMapMapView.addView(mapView)

    }

    private fun addMarker(store: Store) {
        val marker = MapPOIItem()
        marker.itemName = "Default Marker"
        marker.tag = 1
        marker.mapPoint =
            MapPoint.mapPointWithGeoCoord(store.addrY.toDouble(), store.addrX.toDouble())
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.store_map_location
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 1.0f)



//        mapView.addCircle(mapCircle)

        mapView.addPOIItem(marker)
    }

    private fun circleSearch(mapPoint:MapPoint){
        searchViewModel.addry.clear()
        searchViewModel.addrx.clear()
        val mapCircle = MapCircle(
            mapPoint,
            300,
            Color.argb(128, 255, 0, 0),
            Color.argb(128, 255, 255, 0)
        )
        val mapPointBounds=mapCircle.bound
        searchViewModel.addry.add(mapPointBounds.bottomLeft.mapPointGeoCoord.latitude)
        searchViewModel.addry.add(mapPointBounds.topRight.mapPointGeoCoord.latitude)
        searchViewModel.addrx.add(mapPointBounds.bottomLeft.mapPointGeoCoord.longitude)
        searchViewModel.addrx.add(mapPointBounds.topRight.mapPointGeoCoord.longitude)
        searchViewModel.getStoreSearchList()
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        val mapPoint: MapPoint? = p1
        searchViewModel.kakaoMapPoint.postValue(mapPoint)
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
    }


}