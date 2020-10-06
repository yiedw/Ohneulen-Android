package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.data.model.SearchStore
import com.goodchoice.android.ohneulen.data.model.Store
import com.goodchoice.android.ohneulen.databinding.SearchBinding
import com.goodchoice.android.ohneulen.ui.MainActivity
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.ui.dialog.LoadingDialog
import com.goodchoice.android.ohneulen.ui.login.LoginViewModel
import com.goodchoice.android.ohneulen.ui.store.StoreAppBar
import com.goodchoice.android.ohneulen.ui.store.StoreFragment
import com.goodchoice.android.ohneulen.ui.store.StoreViewModel
import com.goodchoice.android.ohneulen.util.addMainFragment
import com.goodchoice.android.ohneulen.util.dpToPx
import com.goodchoice.android.ohneulen.util.replaceAppbarFragment
import com.google.maps.android.ui.IconGenerator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.search.*
import net.daum.mf.map.api.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class Search : Fragment(), MapView.POIItemEventListener, MapView.MapViewEventListener {

    companion object {
        fun newInstance() = Search()
    }

    private var switchOn = false

    //searchStat    0 -> 지도가 전체를 덮음
    //              1 -> 반정도만 덮음
    //              2 -> 지도가 안보임
    private var searchStat = 0

    private val ONE_MARKER = 0
    private val MARKER_LIST = 1

    private val searchStat1Y = 262.dpToPx()

    private lateinit var binding: SearchBinding
    private val searchViewModel: SearchViewModel by inject()
    private val storeViewModel: StoreViewModel by inject()
    private val mainViewModel: MainViewModel by viewModel()


    private lateinit var mapView: MapView
    private lateinit var mapViewContainer: ViewGroup
    private lateinit var locationManager: LocationManager

    private lateinit var storeListHashMap: HashMap<Int, ArrayList<Store>>

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
        storeListHashMap = HashMap()

        //바인딩
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            binding.fragment = this@Search
            viewModel = searchViewModel
        }
        mapView = MapView(requireContext())
        mapViewContainer = binding.searchMap
        mapView.setZoomLevel(3, false)

        if (mapViewContainer.childCount == 0) {
            mapViewContainer.addView(mapView)
            binding.searchInfoCon.bringToFront()
        }
        getCurrentLocationCheck()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerviewSetting
        val adapter = SearchStoreAdapter()
        binding.searchStoreRv.adapter = adapter

        //search List 를 새로고침 해야하는지 여부
        searchViewModel.refreshCheck.observe(viewLifecycleOwner, Observer {
            if (it) {
                //RecyclerviewSetting
//                searchViewModel.getSearchStoreList()
                //평점 좋아요수 후기 좋아요여부 업데이트

                adapter.currentList[adapter.mAdapterPosition].apply {
                    like = storeViewModel.storeFavoriteIsChecked
                    P_1 = storeViewModel.storePoint
                    likeCnt = storeViewModel.storeLikeCnt
                    reviewCnt = storeViewModel.storeReviewCnt
                }
                adapter.notifyItemChanged(adapter.mAdapterPosition)
                searchViewModel.refreshCheck.postValue(false)
            }
        })

        var rvFirstScroll = true

        LoginViewModel.isLogin.observe(viewLifecycleOwner, Observer {
            rvFirstScroll = true

            if (searchViewModel.kakaoMapPoint.value != null) {
                circleSearch(searchViewModel.kakaoMapPoint.value!!)
            }
        })

        //맵을 클릭하는순간 지도로 리스트를 덮기
        mapView.setOnTouchListener { v, event ->
            if (searchStat != 0) {
                slideDown(
                    binding.searchMap.height - binding.searchInfoCon.height.toFloat(),
                    //view y가 아직 정해지지않았기때문에 MainActivity UI 좌표를 가져다 씀
                    MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height,
                    //높이는 그대로
                    binding.searchStoreFrameLayout.height
                )
                searchStat = 0  //맵이 리스트를 덮은상태
            }
            false
        }

        //searchStat=1 인 상태에서 list를 스크롤하면 자동으로 리스트가 지도를 덮음
        binding.searchStoreRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (searchStat == 1 && isRecyclerScrollable()&& newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    && rvFirstScroll
                    rvFirstScroll = false
                    //search List 가 짤려나오는 현상을 해결하기 위해 드래그시에는 순간적으로 뷰 높이를 디스플레이 높이로 설정
                    binding.searchStoreFrameLayout.layoutParams.also { lp ->
                        lp.height =
                            (MainActivity.bottomNav.y + MainActivity.bottomNav.height).toInt()
                    }
                    binding.searchStoreFrameLayout.requestLayout()   //뷰 새로고침
                    slideUp(
                        0f,
                        binding.searchInfoCon.height.toFloat(),
                        //searchStoreFrameLayout height 전체 뷰에 맞게 재설정
                        (MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height - binding.searchInfoCon.height).toInt()
                    )
//                    binding.searchStoreFrameLayout.requestLayout()       // 레이아웃 새로고침
                    binding.searchSlide.visibility = View.GONE    // 슬라이드이미지 숨김
                    binding.searchOpen.visibility = View.VISIBLE  // open 이미지 보여줌
                    binding.searchInfoCon.setBackgroundColor(requireContext().getColor(R.color.white))    //뒤에 지도배경 삭제
                    Timer("seachStat", false).schedule(400) {
                        searchStat = 2  //리스트가 맵을 덮은상태

                    }
                }

                //리스트가 맵을 덮은상태에서 위로 당길때 리스트가 접히게
                else if (searchStat == 2 && !binding.searchStoreRv.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    slideDown(
                        searchStat1Y.toFloat(),
                        searchStat1Y.toFloat() + binding.searchInfoCon.height,
                        (MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height - binding.searchInfoCon.y - binding.searchInfoCon.height).toInt()
                    )
                    //다시 배경에 지도 보이게
                    binding.searchInfoCon.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_new
                    )
                    searchStat = 1  //지도가 반만 덮은상태
                }
            }

        })

        //마커이벤트
        mapView.setPOIItemEventListener(this)
        //맵뷰 이벤트
        mapView.setMapViewEventListener(this)

        //매장 정보 드래그
        var initTouchEventY = 0f    // 초기 터치이벤트 y
        var initY = 0f                // 드래그 이후 뷰 위치를 판별하기 위해 사용


        binding.searchInfoCon.setOnTouchListener { v, event ->
            if (searchStat == 2) {
                //searchStat=2 --> searchStat=1로 갈때 지금은 2-->0으로 변경
//                slideDown(
//                    searchStat1Y.toFloat(),
//                    searchStat1Y.toFloat() + v.height,
//                    (MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height - v.y - v.height).toInt()
//                )
//                //다시 배경에 지도 보이게
//                v.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.background_new
//                )
//                searchStat = 1  //지도가 반만 덮은상태
                slideDown(
                    binding.searchMap.height - v.height.toFloat(),
                    //view y가 아직 정해지지않았기때문에 MainActivity UI 좌표를 가져다 씀
                    MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height,
                    //높이는 그대로
                    binding.searchStoreFrameLayout.height
                )
                searchStat = 0  //맵이 리스트를 덮은상태
                //다시 배경에 지도 보이게
                v.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_new
                )
                binding.searchSlide.visibility = View.VISIBLE    // 슬라이드이미지 보여줌
                binding.searchOpen.visibility = View.GONE  // open 이미지 숨김

                return@setOnTouchListener false
            }
            when (event.action) {
                //뷰를 터치했을 때 최초 한번만 발생하는 Action
                MotionEvent.ACTION_DOWN -> {
                    initTouchEventY = event.y
                    initY = v.y

                }
                //뷰를 드래그 했을 때 지속적으로 발생하는 Action
                MotionEvent.ACTION_MOVE -> {
                    //appbar 높이
                    val appBarHeight = MainActivity.appbarFrameLayout.height
                    //statusBar 높이
                    val statusBarHeight =
                        requireContext().resources.getDimensionPixelSize(R.dimen.status_bar_height)

                    //getRawY -> 화면의 절대 좌표를 제공
                    //getY -> 방법에따라 절대좌표 or 상대좌표를 제공
                    //절대좌표 - appbarHeight - statusBarHeight - 초기 터치위치의 y값 (상대좌표)
                    v.y = (event.rawY - appBarHeight - statusBarHeight - initTouchEventY)
                    binding.searchStoreFrameLayout.y = v.y + v.height

                    //search List 가 짤려나오는 현상을 해결하기 위해 드래그시에는 순간적으로 뷰 높이를 디스플레이 높이로 설정
                    binding.searchStoreFrameLayout.layoutParams.also { lp ->
                        lp.height =
                            (MainActivity.bottomNav.y + MainActivity.bottomNav.height).toInt()
                    }
                    binding.searchStoreFrameLayout.requestLayout()   //뷰 새로고침

                }
                MotionEvent.ACTION_UP -> {
                    if (searchStat == 0) {      //지도가 전체를 덮은경우일때
                        if (v.y < initY) {       //터치를 놨을때 기존위치보다 높을경우
                            slideUp(
                                searchStat1Y.toFloat(),
                                searchStat1Y + v.height.toFloat(),
                                //searchStoreFrameLayout height 전체 뷰에 맞게 재설정
                                (MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height - searchStat1Y - v.height).toInt()
                            )
//                            binding.searchMap.translationY =
//                                (searchStat1Y - binding.searchMap.height) / 2.toFloat()  //map 중앙이 가려지기때문에 조금 이동(범위를 늘려서 필요없음)
                            searchStat = 1  //지도가 반만 덮은 상태
                        } else {
                            slideUp(
                                initY,
                                initY + binding.searchInfoCon.height,
                                binding.searchStoreFrameLayout.height
                            )
                        }
                    }
                    //지도가 반만 덮은상태일때
                    else if (searchStat == 1) {
                        if (v.y < initY) {
                            slideUp(
                                0f,
                                binding.searchInfoCon.height.toFloat(),
                                //searchStoreFrameLayout height 전체 뷰에 맞게 재설정
                                (MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height - v.height).toInt()
                            )
                            binding.searchStoreFrameLayout.requestLayout()       // 레이아웃 새로고침
                            binding.searchSlide.visibility = View.GONE    // 슬라이드이미지 숨김
                            binding.searchOpen.visibility = View.VISIBLE  // open 이미지 보여줌
                            v.setBackgroundColor(requireContext().getColor(R.color.white))    //뒤에 지도배경 삭제
                            searchStat = 2  //리스트가 맵을 덮은상태

                        } else {
                            slideDown(
                                binding.searchMap.height - v.height.toFloat(),
                                //view y가 아직 정해지지않았기때문에 MainActivity UI 좌표를 가져다 씀
                                MainActivity.bottomNav.y - MainActivity.appbarFrameLayout.height,
                                //높이는 그대로
                                binding.searchStoreFrameLayout.height
                            )
//                            binding.searchMap.translationY = 0f     //지도위치를 다시 중앙으로 이동(범위를 늘려서 필요없음)
                            searchStat = 0  //맵이 리스트를 덮은상태
                        }
                    }


                }

            }
            true
        }

        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (it) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage.postValue(false)
                }
            }
        )
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { it ->
                mapView.moveCamera(CameraUpdateFactory.newMapPoint(it))
                rvFirstScroll = true
                circleSearch(it)
            }
        )

        searchViewModel.searchStoreList.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it.isEmpty()) {     //검색결과없을때
                    binding.searchNone.visibility = View.VISIBLE
                    binding.searchStoreRv.visibility = View.GONE
                } else {
                    binding.searchNone.visibility = View.GONE
                    binding.searchStoreRv.visibility = View.VISIBLE
                }

                binding.searchStoreAmount2.text = it.size.toString()
                if (searchViewModel.recyclerViewState != null) {
                    binding.searchStoreRv.layoutManager!!.onRestoreInstanceState(searchViewModel.recyclerViewState)
                    searchViewModel.recyclerViewState = null
                }


                //마커추가
                mapView.removeAllPOIItems()
                for (i in it) {
                    addMarker(i)
                }
                //클러스터 추가
//                val storeList = mutableListOf<Store>()
//                for (i in it.indices) {
//                    if (i == 0) {
//                        storeList.add(it[i])
//                    } else {
//                        if (it[i].addrY == it[i - 1].addrY && it[i].addrX == it[i - 1].addrX) {
//                            storeList.add(it[i])
//                        } else {
//                            if (storeList.size == 1) {
//                                addMarker(storeList[0])
//                            } else {
//                                addMarkerList(i, storeList)
//                            }
//                            storeList.clear()
//                            storeList.add(it[i])
//                        }
//                    }
//
////                    addMarker(it[i])
//                }
//                if (storeList.size == 1) {
////                    Timber.e(storeList[0].storeName)
//                    addMarker(storeList[0])
//                } else {
//                    addMarkerList(it.size, storeList)
//                }
            }
        })
        MainActivity.bottomNav.visibility = View.VISIBLE

        //markerClickEvent

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewContainer.removeAllViews()
        searchViewModel.recyclerViewState =
            binding.searchStoreRv.layoutManager!!.onSaveInstanceState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchViewModel.recyclerViewState =
            binding.searchStoreRv.layoutManager!!.onSaveInstanceState()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }


    private fun circleSearch(mapPoint: MapPoint) {
        val mapCircle = MapCircle(
            mapPoint,
            700,
            Color.argb(128, 255, 0, 0),
            Color.argb(128, 255, 255, 0)
        )
        val mapPointBounds = mapCircle.bound
//        if (searchViewModel.addry.isNotEmpty()) {
////            Timber.e("addry = "+mapPoint.mapPointGeoCoord.latitude)
////            Timber.e("addrx = "+mapPoint.mapPointGeoCoord.longitude)
//            if (searchViewModel.addry[0] == mapPointBounds.bottomLeft.mapPointGeoCoord.latitude
//                && searchViewModel.addry[1] == mapPointBounds.topRight.mapPointGeoCoord.latitude
//                && searchViewModel.addrx[0] == mapPointBounds.bottomLeft.mapPointGeoCoord.longitude
//                && searchViewModel.addrx[1] == mapPointBounds.topRight.mapPointGeoCoord.longitude
//            ) {
////                Timber.e(searchViewModel.addry[0].toString())
////                Timber.e(searchViewModel.addry[1].toString())
////                Timber.e(searchViewModel.addrx[0].toString())
////                Timber.e(searchViewModel.addrx[1].toString())
//                return
//            }
//        }
        searchViewModel.addry.clear()
        searchViewModel.addrx.clear()
        searchViewModel.addry.add(mapPointBounds.bottomLeft.mapPointGeoCoord.latitude)
        searchViewModel.addry.add(mapPointBounds.topRight.mapPointGeoCoord.latitude)
        searchViewModel.addrx.add(mapPointBounds.bottomLeft.mapPointGeoCoord.longitude)
        searchViewModel.addrx.add(mapPointBounds.topRight.mapPointGeoCoord.longitude)
        searchViewModel.getSearchStoreList()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationCheck() {
        if (!mainViewModel.currentLocationSearch)
            return
        else {
            mainViewModel.currentLocationSearch = false
        }
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


    private fun addMarker(store: SearchStore) {
        val marker = MapPOIItem()
        marker.itemName = store.storeName       // 아이템이름
        marker.tag = store.seq.toInt()
        val mapPoint = MapPoint.mapPointWithGeoCoord(store.addrY.toDouble(), store.addrX.toDouble())
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.customImageResourceId = R.drawable.store_map_location
        marker.isCustomImageAutoscale = false
        marker.setCustomImageAnchor(0.5f, 0.5f)

        val markerBalloon = layoutInflater.inflate(R.layout.marker_custom_balloon, null)
        markerBalloon.findViewById<TextView>(R.id.marker_custom_balloon_tv).text =
            store.storeName
        marker.customCalloutBalloon = markerBalloon

        mapView.addPOIItem(marker)
    }

    private fun addMarkerList(
        index: Int,
        storeList: List<Store>
    ) {
        val store = storeList[0]
        val marker = MapPOIItem()
        val mapPoint = MapPoint.mapPointWithGeoCoord(store.addrY.toDouble(), store.addrX.toDouble())
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.CustomImage
        marker.tag = MARKER_LIST
        marker.itemName = index.toString()
        //말풍선 안보이게 가리기
        marker.isShowCalloutBalloonOnTouch = false

        //layout -> Bitmap
//        val layoutInflater =
//            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val markerView = layoutInflater.inflate(R.layout.cluster_icon_unselected, null)
//        markerView.findViewById<TextView>(R.id.cluster_icon_unselected).text =
//            storeList.size.toString()
//        markerView.measure(
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        )
//        markerView.layout(0,0,markerView.measuredWidth,markerView.measuredHeight)
////
//        val markerBitmap = Bitmap.createBitmap(
//            markerView.measuredWidth,
//            markerView.measuredHeight,
//            Bitmap.Config.ARGB_8888
//        )
        val iconGenerator = IconGenerator(requireContext())
        val layoutInflater: LayoutInflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val markerView = layoutInflater.inflate(R.layout.cluster_icon_unselected, null)
        markerView.findViewById<TextView>(R.id.cluster_icon_unselected).text =
            storeList.size.toString()
        iconGenerator.setContentView(markerView)
        iconGenerator.setBackground(null)
        marker.customImageBitmap = iconGenerator.makeIcon()
        marker.setCustomImageAnchor(0.5f, 0.5f)
        mapView.addPOIItem(marker)

        storeListHashMap[index] = ArrayList(storeList)
    }

    private fun slideUp(
        searchInfoConY: Float,
        searchStoreFrameLayoutY: Float,
        searchStoreFrameLayoutHeight: Int
    ) {
        binding.searchStoreFrameLayout.animate()
            .y(searchStoreFrameLayoutY).setDuration(400)
            .withEndAction {        //액션이 끝났을때
                binding.searchStoreFrameLayout.y =
                    searchStoreFrameLayoutY
                binding.searchStoreFrameLayout.layoutParams.also { lp ->
                    lp.height =
                        searchStoreFrameLayoutHeight     //searchStoreRv height 전체 뷰에 맞게 재설정
                }
                //뷰 새로고침
                binding.searchStoreFrameLayout.requestLayout()
            }
            .withStartAction {         //infoCon ,search list, map 애니메이션을 동시에 실행
                binding.searchInfoCon.animate().y(searchInfoConY)
                    .setDuration(400)
                    .withEndAction {
                        binding.searchInfoCon.y = searchInfoConY
                    }.start()
            }


    }

    private fun slideDown(
        searchInfoConY: Float,
        searchStoreFrameLayoutY: Float,
        searchStoreFrameLayoutHeight: Int
    ) {
        binding.searchInfoCon.animate()
            .y(searchInfoConY)
            .setDuration(400)
            .withEndAction {
                binding.searchInfoCon.y = searchInfoConY
            }.withStartAction {
                binding.searchStoreFrameLayout.animate()
                    .y(searchStoreFrameLayoutY)
                    .setDuration(400)
                    .withEndAction {
                        binding.searchStoreFrameLayout.y = searchStoreFrameLayoutY
                        binding.searchStoreFrameLayout.layoutParams.also { lp ->
                            lp.height = searchStoreFrameLayoutHeight
                        }
                        //뷰 새로고침
                        binding.searchStoreFrameLayout.requestLayout()

                    }
            }.start()
    }

    //recyclerview 스크롤 여부
    private fun isRecyclerScrollable(): Boolean {
        val layoutManager = binding.searchStoreRv.layoutManager as LinearLayoutManager
        val adapter = binding.searchStoreRv.adapter as RecyclerView.Adapter
        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.itemCount - 1
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    //marker 위에 말풍선을 터치했을때
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        StoreFragment.storeSeq = p1!!.tag.toString()
        val dialog = LoadingDialog.newInstance("매장 들어가는 중...")
        dialog.show(MainActivity.supportFragmentManager, "loading")
        replaceAppbarFragment(StoreAppBar.newInstance())
        addMainFragment(StoreFragment.newInstance(), true)
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }

    override fun onPOIItemSelected(p0: MapView?, marker: MapPOIItem?) {
//        (binding.searchStoreRv.adapter as SearchStoreAdapter).submitList(storeListHashMap[marker.itemName.toInt()])

    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

    }

    override fun onMapViewInitialized(p0: MapView?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        searchViewModel.kakaoMapPoint.postValue(mapView.mapCenterPoint)
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }


}