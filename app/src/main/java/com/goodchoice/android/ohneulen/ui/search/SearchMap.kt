package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
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
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchMapBinding
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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

    private lateinit var locationManager: LocationManager
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
        mapViewContainer = binding.searchMapMapContainer
        mapViewContainer.addView(mapView)
        getCurrentLocationCheck()
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.setOnTouchListener { _, _ -> true }
        //초기세팅 강남역
//        val myLocation = LatLng(37.4980854357918, 127.028000275071)
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        //맵 포인트가 바뀌면 바로 반영
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { it ->
                Timber.e(it.mapPointGeoCoord.latitude.toString())
                mapView.moveCamera(CameraUpdateFactory.newMapPoint(it))
                circleSearch(it)
            }
        )
        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {
            Timber.e(it.size.toString())
//            addCluster(it, googleMap)
        })
    }

    override fun onPause() {
        super.onPause()
//        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


//    private fun addCluster(storeList: List<Store>, googleMap: GoogleMap) {
//        clusterManager.clearItems()
//        for (i in storeList.indices) {
//            val clusterItem = object : ClusterItem {
//                override fun getSnippet(): String? {
//                    return null
//                }
//
//                override fun getTitle(): String? {
//                    return null
//                }
//
//                override fun getPosition(): LatLng {
//                    return LatLng(storeList[i].addrY.toDouble(), storeList[i].addrX.toDouble())
//                }
//            }
//            clusterManager.addItem(clusterItem)
//        }
//
//        clusterManager.cluster()
//    }

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
        searchViewModel.getSearchStoreList()
    }

//    private fun clusterManagerSetting(googleMap: GoogleMap) {
//        //clusterManager setting
//
//        clusterManager = ClusterManager(requireContext(), googleMap)
//        val clusterRenderer = object :
//            DefaultClusterRenderer<ClusterItem>(requireContext(), googleMap, clusterManager) {
//            val clusterIconGenerator = IconGenerator(requireContext())
//
//            //마커 렌더링링
//            override fun onBeforeClusterItemRendered(
//                item: ClusterItem,
//                markerOptions: MarkerOptions
//            ) {
//                val layoutInflater: LayoutInflater =
//                    requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                val markerView = layoutInflater.inflate(R.layout.cluster_icon_unselected, null)
//                markerView.findViewById<TextView>(R.id.cluster_icon_unselected).text = "1"
//                clusterIconGenerator.setContentView(markerView)
//                clusterIconGenerator.setBackground(null)
//
//                val icon = unSelectedBitmapIcon()
//                markerOptions.icon(icon)
//            }
//
//            //클러스터 렌더링
//            override fun onBeforeClusterRendered(
//                cluster: Cluster<ClusterItem>,
//                markerOptions: MarkerOptions
//            ) {
//                val icon = unSelectedBitmapIcon(cluster)
//                markerOptions.icon(icon)
//            }
//
//        }
//        var selectedCluster: Cluster<ClusterItem>? = null
//        var selectedItem: ClusterItem? = null
//        //1개짜리클릭시
//        clusterManager.setOnClusterItemClickListener {
//            if (selectedItem != null) {
//                val lastMarker = clusterRenderer.getMarker(selectedItem)
//                lastMarker.setIcon(unSelectedBitmapIcon())
//            }
//            if (selectedCluster != null) {
//                val lastCluster = clusterRenderer.getMarker(selectedCluster)
//                lastCluster.setIcon(unSelectedBitmapIcon(selectedCluster))
//
//            }
//            selectedItem = it
//            val newMarker = clusterRenderer.getMarker(it)
//            newMarker.setIcon(selectedBitmapIcon())
//            true
//        }
//
//        //클러스터클릭시
//        clusterManager.setOnClusterClickListener {
//            if (selectedCluster != null) {
//                val lastCluster = clusterRenderer.getMarker(selectedCluster)
//                lastCluster.setIcon(unSelectedBitmapIcon(selectedCluster))
//
//            }
//            if (selectedItem != null) {
//                val lastMarker = clusterRenderer.getMarker(selectedItem)
//                lastMarker.setIcon(unSelectedBitmapIcon())
//            }
//            selectedCluster = it
//            val newCluster = clusterRenderer.getMarker(it)
//            newCluster.setIcon(selectedBitmapIcon(selectedCluster))
//            true
//        }
//
//        clusterManager.renderer = clusterRenderer
//    }

//    private fun unSelectedBitmapIcon(cluster: Cluster<ClusterItem>? = null): BitmapDescriptor {
//        val iconGenerator = IconGenerator(requireContext())
//        val layoutInflater: LayoutInflater =
//            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val markerView = layoutInflater.inflate(R.layout.cluster_icon_unselected, null)
//        if (cluster == null) {
//            markerView.findViewById<TextView>(R.id.cluster_icon_unselected).text = "1"
//        } else {
//            markerView.findViewById<TextView>(R.id.cluster_icon_unselected).text =
//                cluster.size.toString()
//        }
//        iconGenerator.setContentView(markerView)
//        iconGenerator.setBackground(null)
//        val icon = iconGenerator.makeIcon()
//        return BitmapDescriptorFactory.fromBitmap(icon)
//    }
//
//    private fun selectedBitmapIcon(cluster: Cluster<ClusterItem>? = null): BitmapDescriptor {
//        val iconGenerator = IconGenerator(requireContext())
//        val layoutInflater: LayoutInflater =
//            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val markerView = layoutInflater.inflate(R.layout.cluster_icon_selected, null)
//        if (cluster == null) {
//            markerView.findViewById<TextView>(R.id.cluster_icon_selected).text = "1"
//        } else {
//            markerView.findViewById<TextView>(R.id.cluster_icon_selected).text =
//                cluster.size.toString()
//        }
//        iconGenerator.setContentView(markerView)
//        iconGenerator.setBackground(null)
//        val icon = iconGenerator.makeIcon()
//        return BitmapDescriptorFactory.fromBitmap(icon)
//    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationCheck() {
        if (!mainViewModel.currentLocationSearch)
            return
        mainViewModel.currentLocationSearch = false
        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
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
}