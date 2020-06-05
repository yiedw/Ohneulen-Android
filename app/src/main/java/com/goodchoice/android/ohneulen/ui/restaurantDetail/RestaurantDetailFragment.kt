package com.goodchoice.android.ohneulen.ui.restaurantDetail

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.goodchoice.android.ohneulen.MainActivity
import com.goodchoice.android.ohneulen.R
import com.goodchoice.android.ohneulen.databinding.RestaurantdetailFragmentBinding
import timber.log.Timber

class RestaurantDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RestaurantDetailFragment()
    }

    private lateinit var binding: RestaurantdetailFragmentBinding
    private var state=1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        MainActivity.appbarFrameLayout.visibility=View.GONE
        Timber.e(MainActivity.mainFrameLayout.y.toString())
        //나중에 되돌리기위한 y
        val initY = MainActivity.mainFrameLayout.y
        MainActivity.mainFrameLayout.y = 0f
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.restaurantdetail_fragment,
            container,
            false
        )
        //어둡게 만들기
        binding.restaurantDetailBigImage.setColorFilter(
            ContextCompat.getColor(activity!!, R.color.colorTransparentBlack),
            PorterDuff.Mode.SRC_OVER
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val childFragment=childFragmentManager
        childFragment.beginTransaction().replace(R.id.restaurantDetail_frameLayout,
        RestaurantDetailHomeFragment.newInstance()).commitNow()
        stickyHeader()


    }

    fun stickyHeader(){
        if(state==1){
            binding.restaurantDetailNewScrollView.run {
                header=binding.restaurantDetailFrameLayout
                stickListener={ _ ->
                    Timber.e("asdf")
                }
            }
        }
    }


}