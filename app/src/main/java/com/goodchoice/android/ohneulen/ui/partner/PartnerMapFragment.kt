package com.goodchoice.android.ohneulen.ui.partner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.PartnerMapFragmentBinding
import kotlinx.android.synthetic.main.partner_map_fragment.*
import net.daum.mf.map.api.MapView
import timber.log.Timber

class PartnerMapFragment :Fragment(){

    private lateinit var binding:PartnerMapFragmentBinding
    companion object{
        fun newInstance()=PartnerMapFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(
            inflater,
            R.layout.partner_map_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val mapView = MapView(context)
            mapView.setZoomLevel(2, false)
            val mapViewContainer: ViewGroup = partner_mapView as ViewGroup
            mapViewContainer.addView(mapView)
        } catch (e: UnsatisfiedLinkError) {
            Timber.e(e)
        } catch (e: NoClassDefFoundError) {
            Timber.e(e)
        }
    }
}