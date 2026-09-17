package com.example.prog7314.features.home.data

import com.example.prog7314.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder

/**
 * Queries the Wikipedia OpenSearch API to get city/place name suggestions.
 * Returns up to 8 matching article titles.
 */
object WikipediaCitySearch {

    suspend fun search(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val encoded = URLEncoder.encode(query.trim(), "UTF-8")
        val url = "https://en.wikipedia.org/w/api.php" +
            "?action=opensearch&search=$encoded&limit=8&namespace=0&format=json"
        try {
            val request = Request.Builder().url(url).get().build()
            val body = HttpClient.instance.newCall(request).execute().use { resp ->
                resp.body?.string()
            } ?: return@withContext emptyList()
            // OpenSearch response: [query, [titles], [descriptions], [urls]]
            val root = JSONArray(body)
            val titles = root.getJSONArray(1)
            List(titles.length()) { titles.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
