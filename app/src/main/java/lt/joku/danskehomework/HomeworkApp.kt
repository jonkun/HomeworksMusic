package lt.joku.danskehomework

import android.app.Application
import com.carto.ui.MapView
import com.facebook.stetho.Stetho
import lt.joku.danskehomework.service.MusicbrainzService
import lt.joku.danskehomework.service.RetrofitProvider


/**
 * @author Jonas Kundra
 * @since 1.0
 */

class HomeworkApp: Application() {

    lateinit var musicbrainzService: MusicbrainzService

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) initStetho()

        val isRegistered = registerMapLicense()
        if (!isRegistered) {
            throw IllegalStateException("Carto map key expired")
        }

        musicbrainzService = RetrofitProvider.createMusicbrainzService()
    }

    fun registerMapLicense() =
            MapView.registerLicense(getString(R.string.carto_db_map_key), this)

    fun initStetho() =
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())


}