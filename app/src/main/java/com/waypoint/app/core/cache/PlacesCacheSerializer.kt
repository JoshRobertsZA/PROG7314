package com.waypoint.app.core.cache

import org.json.JSONArray
import org.json.JSONObject

// Shared by the per-user local cache (PlacesCacheLocalRepository) and the
// shared cache read path in LocationIQRepository, so both sides agree on one
// PlacesCache <-> JSON shape.
object PlacesCacheSerializer {

    fun toJson(data: PlacesCache): JSONObject {
        val placesArray = JSONArray().apply {
            data.places.forEach { p ->
                put(JSONObject().apply {
                    put("id", p.id)
                    put("name", p.name)
                    put("type", p.type)
                    put("category", p.category)
                    put("lat", p.lat)
                    put("lon", p.lon)
                    put("displayAddress", p.displayAddress)
                    put("distanceMetres", p.distanceMetres)
                })
            }
        }
        return JSONObject().apply {
            put("city", data.city)
            put("fetchedDateUtc", data.fetchedDateUtc)
            put("places", placesArray)
        }
    }

    fun fromJson(json: JSONObject): PlacesCache? = try {
        val placesArray = json.getJSONArray("places")
        val places = (0 until placesArray.length()).map { i ->
            val p = placesArray.getJSONObject(i)
            ExplorePlace(
                id             = p.getString("id"),
                name           = p.getString("name"),
                type           = p.getString("type"),
                category       = p.getString("category"),
                lat            = p.getDouble("lat"),
                lon            = p.getDouble("lon"),
                displayAddress = p.getString("displayAddress"),
                distanceMetres = p.getInt("distanceMetres"),
            )
        }
        PlacesCache(
            city           = json.getString("city"),
            fetchedDateUtc = json.getString("fetchedDateUtc"),
            places         = places,
        )
    } catch (e: Exception) {
        null
    }
}
