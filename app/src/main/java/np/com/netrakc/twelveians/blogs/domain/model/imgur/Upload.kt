package np.com.netrakc.twelveians.blogs.domain.model.imgur

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Upload(
    @Json(name = "account_id")
    val account_id: Int?,
    @Json(name = "account_url")
    val account_url: String?,
    @Json(name = "ad_type")
    val ad_type: Int?,
    @Json(name = "ad_url")
    val ad_url: String?,
    @Json(name = "animated")
    val animated: Boolean,
    @Json(name = "bandwidth")
    val bandwidth: Int?,
    @Json(name = "datetime")
    val datetime: Long,
    @Json(name = "deletehash")
    val deletehash: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "favorite")
    val favorite: Boolean,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: String,
    @Json(name = "in_gallery")
    val in_gallery: Boolean,
    @Json(name = "in_most_viral")
    val in_most_viral: Boolean,
    @Json(name = "is_ad")
    val is_ad: Boolean,
    @Json(name = "link")
    val link: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "nsfw")
    val nsfw: Any?,
    @Json(name = "section")
    val section: Any?,
    @Json(name = "size")
    val size: Int,
    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "title")
    val title: String?,
    @Json(name = "type")
    val type: String,
    @Json(name = "views")
    val views: Int,
    @Json(name = "vote")
    val vote: Any?,
    @Json(name = "width")
    val width: Int
)