package lt.joku.danskehomework

import android.app.Application
import com.carto.ui.MapView
import com.facebook.stetho.Stetho


/**
 * @author Jonas Kundra
 * @since 1.0
 */

class HomeworkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) initStetho()

        val isRegistered = registerMapLicense()
        if (!isRegistered) {
            throw IllegalStateException("Carto map key expired")
        }
    }

    fun registerMapLicense() =
            MapView.registerLicense(getString(R.string.carto_db_map_key), this)

    fun initStetho() =
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())


}