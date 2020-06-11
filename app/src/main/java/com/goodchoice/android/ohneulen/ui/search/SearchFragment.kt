package com.goodchoice.android.ohneulen.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.SearchFragmentBinding
import com.goodchoice.android.ohneulen.util.replaceMainFragment
import kotlinx.android.synthetic.main.search_fragment.*
import net.daum.mf.map.api.MapView
import timber.log.Timber

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var switchOn = false

    private lateinit var binding: SearchFragmentBinding

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //다음지도 불러오기
        //try catch 추후 삭제
        try {
            val mapView = MapView(context)
            mapView.setZoomLevel(2, false)
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
            viewModel = ViewModelProvider(this@SearchFragment).get(SearchViewModel::class.java)

        }


    }

    fun switchClick(view: View) {
        if (!switchOn)
            binding.searchMapView.visibility = View.GONE
        else
            binding.searchMapView.visibility = View.VISIBLE

        switchOn = !switchOn
    }

    fun submitClick(view: View){
        replaceMainFragment(SearchFilterFragment.newInstance())
    }

}