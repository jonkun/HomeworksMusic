package lt.joku.danskehomework.service

import lt.joku.danskehomework.domain.Places
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Jonas Kundra
 * @since 1.0
 */

interface MusicbrainzService {

    @GET("ws/2/place?fmt=json")
    fun places(@Query("query") query: String,
               @Query("limit") limit: String,
               @Query("offset") offset: String): Call<Places>

}
