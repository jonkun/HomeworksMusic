package lt.joku.danskehomework.service

import com.facebook.stetho.okhttp3.StethoInterceptor
import lt.joku.danskehomework.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Kundra
 * @since 1.0
 */

object RetrofitProvider{

    val END_POINT = "https://musicbrainz.org/"

    fun createMusicbrainzService() =
            buildRetrofit(END_POINT).create<MusicbrainzService>(MusicbrainzService::class.java)

    private fun buildRetrofit(url: String) =
            Retrofit.Builder()
                    .client(getHttpClientBuilder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url)
                    .build()

    private fun getHttpClientBuilder(): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addNetworkInterceptor(StethoInterceptor())
        }

        clientBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }

        return clientBuilder
    }
}