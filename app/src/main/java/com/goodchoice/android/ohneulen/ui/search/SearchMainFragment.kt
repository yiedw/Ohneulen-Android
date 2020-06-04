package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchMainFragmentBinding
import kotlinx.android.synthetic.main.search_main_fragment.*
import net.daum.mf.map.api.MapView
import timber.log.Timber
import java.lang.Exception

class SearchMainFragment : Fragment() {

    companion object {
        fun newInstance() = SearchMainFragment()
    }

//    private val mapView by lazy {
//        MapView(context)
//    }

    private lateinit var binding:SearchMainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.search_main_fragment,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //다음지도 불러오기
        //try catch 추후 삭제
        try {
            val mapView = MapView(context)
            mapView.setZoomLevel(2, false)
            val mapViewContainer: ViewGroup = search_main_fragment_mapView as ViewGroup
            mapViewContainer.addView(mapView)
        } catch (e: UnsatisfiedLinkError) {
            Timber.e(e)
        }
        binding.apply {
            lifecycleOwner=this@SearchMainFragment
            viewModel=SearchViewModel()

        }
    }
}