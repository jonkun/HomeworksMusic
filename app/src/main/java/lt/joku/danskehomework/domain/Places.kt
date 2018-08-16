package lt.joku.danskehomework.domain

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author Jonas Kundra
 * @since 1.0
 */

data class Places(
        val created: String,
        val count: Int,
        val offset: Int,
        val places: List<Place>
)

data class Place(
        val id: String,
        val type: String,
        @SerializedName("type-id")
        val typeId: String,
        val score: Int,
        val name: String,
        val disambiguation: String,
        val area: Area,
        @SerializedName("life-span")
        val lifeSpan: LifeSpan?,
        val coordinates: Coordinates?
)

data class LifeSpan(
        @JsonAdapter(DateJsonAdapter::class)
        val begin: Date?,
        val ended: String?
)

data class Area(
        val id: String,
        val type: String,
        val typeId: String,
        val name: String,
        val sortName: String,
        val lifeSpan: LifeSpan
)

data class Coordinates(
        val latitude: Double,
        val longitude: Double
)