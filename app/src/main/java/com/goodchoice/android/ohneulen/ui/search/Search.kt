package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.SearchBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class Search : Fragment(), MapView.POIItemEventListener {

    companion object {
        fun newInstance() = Search()
    }

    private var switchOn = false
    private lateinit var binding: SearchBinding
    private val searchViewModel: SearchViewModel by inject()
    private val mainViewModel: MainViewModel by viewModel()

    private val mapView by lazy {
        MapView(requireContext())
    }
    private lateinit var mapViewContainer: ViewGroup
    private lateinit var locationManager: LocationManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //초기화 (두번씩 observe 되는것 방지)
        searchViewModel.kakaoMapPoint = MutableLiveData()
        searchViewModel.searchStoreList = MutableLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.search,
                container,
                false
            )

        //바인딩
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.fragment = this@Search
            viewModel = searchViewModel
        }
        mapViewContainer = binding.searchMap
        mapView.setZoomLevel(3, false)
        mapViewContainer.addView(mapView)
        getCurrentLocationCheck()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //터치막기
        mapView.setOnDragListener { v, event -> true }
//        mapView.setOnTouchListener { _, _ -> true }

        //marker 이벤트
//        val mapViewPOIItemListener = object : MapView.POIItemEventListener {
//            override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
//                StoreFragment.storeSeq = p1!!.tag.toString()
//                replaceAppbarFragment(StoreAppBar.newInstance())
//                addMainFragment(StoreFragment.newInstance(), true)
//
//            }
//
//            override fun onCalloutBalloonOfPOIItemTouched(
//                p0: MapView?,
//                p1: MapPOIItem?,
//                p2: MapPOIItem.CalloutBalloonButtonType?
//            ) {
//            }
//
//            override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
//            }
//
//            override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
//            }
//        }
        mapView.setPOIItemEventListener(this)


        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (searchViewModel.toastMessage.value!!) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage = MutableLiveData(false)
                }
            }
        )
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { it ->
                mapView.moveCamera(CameraUpdateFactory.newMapPoint(it))
                circleSearch(it)
            }
        )

        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {
            binding.searchStoreAmount.text = "매장 ${it.size}"
            //마커추가
            mapView.removeAllPOIItems()
            for (i in it) {
                addMarker(i)
            }
//            if(it.isEmpty()){
//                Toast.makeText(requireContext(),"검색결과가 없습니다",Toast.LENGTH_SHORT).show()
//            }
        })
        MainActivity.bottomNav.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
    }


    fun switchClick(view: View) {
        if (!switchOn) {
            binding.searchMap.visibility = View.GONE
            Glide.with(requireContext()).load(R.drawable.open).into(binding.searchSwitch)
        } else {
            binding.searchMap.visibility = View.VISIBLE
            Glide.with(requireContext()).load(R.drawable.close).into(binding.searchSwitch)
        }
        switchOn = !switchOn
    }

    private fun circleSearch(mapPoint: MapPoint) {
        searchViewModel.addry.clear()
        searchViewModel.addrx.clear()
        val mapCircle = MapCircle(
            mapPoint,
            300,
            Color.argb(128, 255, 0, 0),
            Color.argb(128, 255, 255, 0)
        )
        val mapPointBounds = mapCircle.bound
        searchViewModel.addry.add(mapPointBounds.bottomLeft.mapPointGeoCoord.latitude)
        searchViewModel.addry.add(mapPointBounds.topRight.mapPointGeoCoord.latitude)
        searchViewModel.addrx.add(mapPointBounds.bottomLeft.mapPointGeoCoord.longitude)
        searchViewModel.addrx.add(mapPointBounds.topRight.mapPointGeoCoord.longitude)
        searchViewModel.getStoreSearchList()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationCheck() {
        if (!mainViewModel.currentLocationSearch)
            return
//        mainViewModel.currentLocationSearch = false
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            return
        }
        //퍼미션 리스너 생성
        val permissionListener = object : PermissionListener {
            //승인
            override fun onPermissionGranted() {
                val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f;
                val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1;
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                }

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener
                    )
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        searchViewModel.currentLocationData(location.latitude, location.longitude)
                    }
                } else if (isGpsEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener
                    )
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location != null) {
                        searchViewModel.currentLocationData(location.latitude, location.longitude)
                    }
                }

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                //거절
                Toast.makeText(requireContext(), "위치 정보를 확인할수 없습니다", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        }
        //권한확인
        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setRationaleMessage("위치정보를 확인하기 위해서는 권한이 필요합니다")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()


    }

    private fun addMarker(store: Store) {
        val marker = MapPOIItem()
        marker.itemName = store.storeName
        marker.tag = store.seq.toInt()
        val mapPoint = MapPoint.mapPointWithGeoCoord(store.addrY.toDouble(), store.addrX.toDouble())
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.store_map_location
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 0.5f)
        mapView.addPOIItem(marker)
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        StoreFragment.storeSeq = p1!!.tag.toString()
        replaceAppbarFragment(StoreAppBar.newInstance())
        addMainFragment(StoreFragment.newInstance(), true)
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }


}