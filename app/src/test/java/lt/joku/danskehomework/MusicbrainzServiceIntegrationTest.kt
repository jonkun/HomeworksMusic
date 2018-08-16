package lt.joku.danskehomework

import lt.joku.danskehomework.service.RetrofitProvider
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author Jonas Kundra
 * @since 1.0
 */
class MusicbrainzServiceIntegrationTest {

    @Test
    fun getPlaaces_isSuccessful() {
        val a = 109 / 10
        System.out.println(a)
    }
    @Test
    fun getPlaces_isSuccessful() {
        val musicbrainzService = RetrofitProvider.createMusicbrainzService()
        val response = musicbrainzService.places("a", "1", "1").execute()
        assertThat(response.isSuccessful, equalTo(true))
        assertThat(response.body()?.places?.size , equalTo(1))
    }
}
