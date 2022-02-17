package np.com.netrakc.twelveians.blogs.presentation

import android.accounts.Account
import android.net.Uri
import java.io.File

sealed class AddPostScreenEvent {
    data class OnSignInResult(val account: Account?) : AddPostScreenEvent()
    data class OnImageLauncherResult(val uris: List<Uri>) : AddPostScreenEvent()
    data class OnImageDeleteClick(val uri: Uri) : AddPostScreenEvent()
    data class OnUploadImagesBtnClick(val files: List<File>) : AddPostScreenEvent()
    data class OnContentTitleChange(val title: String) : AddPostScreenEvent()
    data class OnContentTagsChange(val tags: String) : AddPostScreenEvent()
    data class OnIsDraftCheckChange(val isChecked: Boolean) : AddPostScreenEvent()
    object OnUploadPostClick : AddPostScreenEvent()
}