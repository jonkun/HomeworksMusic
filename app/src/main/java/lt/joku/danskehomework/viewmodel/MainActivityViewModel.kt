package lt.joku.danskehomework.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.support.annotation.WorkerThread
import android.util.Log
import android.view.View
import kotlinx.coroutines.experimental.launch
import lt.joku.danskehomework.domain.Place
import lt.joku.danskehomework.service.RetrofitProvider
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivityViewModel : ViewModel() {

    val TAG = "MainActivityViewModel"

    val TYPING_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(2)
    val PLACES_LIMIT = 10;

    var showProgressBar = MutableLiveData<Int>()
    var places = MutableLiveData<List<Place>>()

    private val musicbrainzService = RetrofitProvider.createMusicbrainzService()
    private val searchStartHandler = Handler()
    private var searchText: String = ""

    @Suppress("UNUSED_PARAMETER")
    fun onSearchTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.w(TAG, "onSearchTextChanged $s")
        if (count == 0) {
            searchStartHandler.removeCallbacks(startSearch)
            places.postValue(emptyList())
            return
        }

        searchText = s.toString()

        searchStartHandler.removeCallbacks(startSearch)
        searchStartHandler.postDelayed(startSearch, TYPING_TIMEOUT_MS)
    }

    val startSearch = Runnable {
        Log.w(TAG, "Search started with text $searchText")

        showProgressBar.postValue(View.VISIBLE)

        launch {
            val loadedPlaces = mutableListOf<Place>()
            val response = musicbrainzService.places(searchText, PLACES_LIMIT.toString(), 1.toString()).execute()
            response.body()?.let { it ->
                loadedPlaces.addAll(it.places)
                val offsets = it.count / PLACES_LIMIT + (if (it.count % PLACES_LIMIT == 0) 0 else 1)
                (2..offsets).forEach {
                    loadedPlaces.addAll(requestPlaces(it))
                }
            }
            val date1990 = GregorianCalendar(1990, 0, 0).time
            val filteredByCoordinates = loadedPlaces.filter { it.coordinates != null}
            val filteredByDate = filteredByCoordinates.filter {
                    it.lifeSpan?.begin != null &&
                    it.lifeSpan.begin.after(date1990)
                    it.lifeSpan?.ended == null}
            places.postValue(filteredByDate)
            showProgressBar.postValue(View.GONE)

            Log.w(TAG, "Search finished")
        }
    }


    @WorkerThread
    fun requestPlaces(offset: Int): List<Place> {
        val response = musicbrainzService.places(searchText, PLACES_LIMIT.toString(), offset.toString()).execute()
        if (response.isSuccessful) {
            response.body()?.let {
                return it.places
            }
        }
        return emptyList()
    }
}