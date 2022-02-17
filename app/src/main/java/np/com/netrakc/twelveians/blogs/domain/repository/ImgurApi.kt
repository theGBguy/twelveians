package np.com.netrakc.twelveians.blogs.domain.repository

import np.com.netrakc.twelveians.blogs.BuildConfig
import np.com.netrakc.twelveians.blogs.domain.model.imgur.Upload
import np.com.netrakc.twelveians.blogs.domain.model.imgur.UploadResponse
import np.com.netrakc.twelveians.blogs.domain.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

// An interface to communicate with imgur's public api to upload post images
interface ImgurApi {

    @Multipart
    @Headers("Authorization: CLIENT-ID $CLIENT_ID")
    @POST("/3/upload")
    suspend fun uploadImage(
        @Part imageFile: MultipartBody.Part?,
        @Part("name") name: RequestBody? = null
    ): Response<UploadResponse>

    interface ImgurTasks {
        suspend fun uploadImage(file: File, title: String? = null): Result<Upload>
    }

    companion object {
        const val CLIENT_ID = BuildConfig.CLIENT_ID
        const val BASE_URL = "https://api.imgur.com"
    }
}