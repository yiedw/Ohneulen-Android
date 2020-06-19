package com.goodchoice.android.ohneulen.ui.search

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.goodchoice.android.ohneulen.ui.MainViewModel
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFragmentBinding
import com.goodchoice.android.ohneulen.util.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var switchOn = false
    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()
    private val mapView by lazy {
        MapView(requireContext())
    }


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

        binding.apply {
            lifecycleOwner = this@SearchFragment
            binding.fragment = this@SearchFragment
            viewModel = searchViewModel
        }
        //mvm 을 이용해서 데이터 받아오기
        searchViewModel.searchEditText = mainViewModel.searchEditText


        //검색어기반
        if (searchViewModel.searchEditText != ConstList.CURRENT_LOCATION) {
            binding.searchEditText.setText(mainViewModel.searchEditText)
            if (!binding.searchEditText.text.toString().isBlank()) {
                //검색어를 이용해서 지도정보 불러오기
                searchViewModel.searchMapData()
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        //다음지도 불러오기
        mapView.setZoomLevel(2, false)
        val mapViewContainer: ViewGroup = binding.searchMapView
        mapViewContainer.addView(mapView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //맵 포인트가 바뀌면 바로 반영
        searchViewModel.kakaoMapPoint.observe(
            viewLifecycleOwner, Observer { t ->
                mapView.setMapCenterPoint(t, false)
            }
        )
        //현재위치기반
        if (mainViewModel.searchEditText == ConstList.CURRENT_LOCATION) {
            //퍼미션 리스너 생성
            val permissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                    //승인
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
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
        }

        //검색어 없을시 토스트 띄우기
        searchViewModel.toastMessage.observe(
            viewLifecycleOwner, Observer {
                if (searchViewModel.toastMessage.value!!) {
                    Toast.makeText(requireContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show()
                    searchViewModel.toastMessage = MutableLiveData(false)
                }
            }
        )


    }

    fun searchCLick(view: View) {
        if (!binding.searchEditText.text.toString().isBlank()) {
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
            searchViewModel.searchEditText = binding.searchEditText.text.toString()
            mainViewModel.searchEditText=binding.searchEditText.text.toString()
            searchViewModel.searchMapData()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_LONG).show()
        }


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