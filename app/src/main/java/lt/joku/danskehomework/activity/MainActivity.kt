package lt.joku.danskehomework.activity

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import lt.joku.danskehomework.R
import lt.joku.danskehomework.databinding.ActivityMainBinding
import lt.joku.danskehomework.service.MainActivityViewModelFactory
import lt.joku.danskehomework.service.MapService
import lt.joku.danskehomework.service.MapServiceImpl
import lt.joku.danskehomework.util.DeviceUtils
import lt.joku.danskehomework.viewmodel.MainActivityViewModel
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Kundra
 * @since 1.0
 */
class MainActivity : AppCompatActivity() {

    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var mapService: MapService

    val makersDeletionHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Initialize viewModels */
        mainActivityViewModel = ViewModelProviders.of(this, MainActivityViewModelFactory()).get(MainActivityViewModel::class.java)

        /** Initialize views */
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = mainActivityViewModel
        }

        /* Create map */
        mapService = MapServiceImpl(mapView)

        /* Register observers */
        mapView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> DeviceUtils.hideKeyboard(this@MainActivity)
            }
            false
        }

        mainActivityViewModel.showProgressBar.observe(this, android.arch.lifecycle.Observer { it ->
            it?.let {
                progressBar.visibility = it
                inputText.isEnabled = it != View.VISIBLE
            }
        })

        mainActivityViewModel.places.observe(this, android.arch.lifecycle.Observer { it ->
            mapService.markersHolder.clear()
            it?.forEach { itPlace ->
                itPlace.coordinates?.let { itCoordinate ->

                    /* Create and add marker into map */
                    val marker = mapService.createMarker(itCoordinate.latitude, itCoordinate.longitude)
                    mapService.markersHolder.add(marker)

                    /* Remove marker after some time */
                    itPlace.lifeSpan?.begin?.let { itBegin ->
                        @Suppress("DEPRECATION")
                        val yearDiff = (Date().year - itBegin.year).toLong()
                        makersDeletionHandler.postDelayed({
                            mapService.markersHolder.remove(marker)
                        },TimeUnit.SECONDS.toMillis(yearDiff))
                    }
                }
            }
            val padding: Int = (50 * this@MainActivity.resources.displayMetrics.density).toInt()
            mapService.zoomInByBounds(mapService.markersHolder, padding)
        })
    }
}
