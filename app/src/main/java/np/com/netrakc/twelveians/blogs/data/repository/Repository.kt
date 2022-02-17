package np.com.netrakc.twelveians.blogs.data.repository

import android.accounts.Account
import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.blogger.Blogger
import com.google.api.services.blogger.BloggerScopes
import com.google.api.services.blogger.model.Post
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import np.com.netrakc.twelveians.blogs.BuildConfig
import np.com.netrakc.twelveians.blogs.domain.model.imgur.Upload
import np.com.netrakc.twelveians.blogs.domain.repository.ImgurApi
import np.com.netrakc.twelveians.blogs.domain.utils.Result
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val imgurApi: ImgurApi,
    private val bloggerBuilder: Blogger.Builder,
    @ApplicationContext private val context: Context
) : ImgurApi.ImgurTasks {

    // upload image to imgur server and return result of the upload such as its url
    override suspend fun uploadImage(
        file: File,
        title: String?
    ): Result<Upload> {
        return withContext(Dispatchers.IO) {
            try {
                val filePart = MultipartBody.Part.createFormData(
                    "image", file.name, RequestBody.create(
                        MediaType.parse("image"),
                        file
                    )
                )

                val response = imgurApi.uploadImage(
                    filePart, RequestBody.create(
                        MediaType.parse("text/plain"),
                        title ?: file.name
                    )
                )

                if (response.isSuccessful) {
                    Result.Success(response.body()!!.upload)
                } else {
                    Result.Failure("Unknown exception ${response.errorBody().toString()}")
                }
            } catch (e: Exception) {
                Result.Failure(e.message)
            }
        }
    }

    // add post in the blogger
    suspend fun addAPostInABlog(content: Post, isDraft: Boolean, account: Account?): Result<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAccountCredential.usingOAuth2(
                    context,
                    Collections.singleton(BloggerScopes.BLOGGER)
                )
                credential.selectedAccount = account
                bloggerBuilder.httpRequestInitializer = credential
                val insert = bloggerBuilder.build().posts().insert(BLOG_ID, content)
                insert.isDraft = isDraft
                Result.Success(insert.execute())
            } catch (e: Exception) {
                Log.d("Repository", "Error occurred while inserting new post : ${e.message}")
                Result.Failure("Error occurred while inserting new post : ${e.message}")
            }
        }
    }

    companion object {
        const val BLOG_ID = BuildConfig.BLOG_ID
    }
}