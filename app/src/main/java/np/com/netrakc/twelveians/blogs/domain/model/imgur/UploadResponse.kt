package np.com.netrakc.twelveians.blogs.domain.model.imgur

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadResponse(
    @Json(name = "data")
    val upload: Upload,
    @Json(name = "status")
    val status: Int,
    @Json(name = "success")
    val success: Boolean
)