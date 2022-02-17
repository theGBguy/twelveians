package np.com.netrakc.twelveians.blogs.presentation

import android.accounts.Account
import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.services.blogger.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import np.com.netrakc.twelveians.blogs.data.repository.Repository
import np.com.netrakc.twelveians.blogs.domain.utils.Result
import np.com.netrakc.twelveians.blogs.presentation.components.ButtonState
import np.com.netrakc.twelveians.blogs.presentation.utils.createPostHTML
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    // button states
    val signInBtnState = mutableStateOf(ButtonState.INITIAL)
    val uploadImageBtnState = mutableStateOf(ButtonState.INITIAL)
    val uploadPostBtnState = mutableStateOf(ButtonState.INITIAL)

    // signed in account
    private var _account: Account? = null

    // list of selected images uri
    private val _imagesUri = mutableStateOf<List<Uri>>(listOf())
    val imagesUri: State<List<Uri>> = _imagesUri

    // "https://i.imgur.com/o2AySZI.jpeg" first image uploaded through this app
    // list of uploaded images link
    private val imagesLinkList = mutableListOf<String>()

    // state variable for text field
    private val _contentTitle = mutableStateOf("")
    val contentTitle: State<String> = _contentTitle
    private val _contentTags = mutableStateOf("")
    val contentTags: State<String> = _contentTags
    private val _isDraft = mutableStateOf(false)
    val isDraft: State<Boolean> = _isDraft

    // emit UI related events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // handle all the events in the Add Post screen
    fun onEvent(event: AddPostScreenEvent) {
        when (event) {
            is AddPostScreenEvent.OnSignInResult -> {
                if (event.account == null) {
                    _account = null
                    signInBtnState.value = ButtonState.INITIAL
                } else {
                    _account = event.account
                    signInBtnState.value = ButtonState.COMPLETED
                }
            }
            is AddPostScreenEvent.OnImageLauncherResult -> {
                _imagesUri.value = event.uris
            }
            is AddPostScreenEvent.OnImageDeleteClick -> {
                _imagesUri.value = _imagesUri.value.filterNot { it == event.uri }
            }
            is AddPostScreenEvent.OnUploadImagesBtnClick -> {
                uploadImages(event.files)
            }
            is AddPostScreenEvent.OnContentTitleChange -> {
                _contentTitle.value = event.title
            }
            is AddPostScreenEvent.OnContentTagsChange -> {
                _contentTags.value = event.tags
            }
            is AddPostScreenEvent.OnIsDraftCheckChange -> {
                _isDraft.value = event.isChecked
            }
            is AddPostScreenEvent.OnUploadPostClick -> {
                addAPostInABlog()
            }
        }
    }

    private fun uploadImages(files: List<File>) {
        viewModelScope.launch {
            files.forEach {
                val result = repository.uploadImage(
                    it,
                    contentTitle.value
                )
                when (result) {
                    is Result.Success -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Image is uploaded successfully : ${result.value.link}"))
                        imagesLinkList.add(result.value.link)
                        uploadImageBtnState.value = ButtonState.COMPLETED
                    }
                    is Result.Failure -> {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Could not upload the image"))
                        uploadImageBtnState.value = ButtonState.INITIAL
                    }
                }
            }
        }
    }

    private fun addAPostInABlog() {
        val post = Post().apply {
            title = _contentTitle.value
            content = createPostHTML(
                title = _contentTitle.value,
                imageUrls = imagesLinkList,
                tags = _contentTags.value
            )
        }

//        val post = Post().apply {
//            title = "Title"
//            content = createPostHTML(
//                title = "Title",
//                imageUrls = imagesLinkList,
//                tags = "Tags"
//            )
//        }

        viewModelScope.launch {
            when (val publishedPostResult =
                repository.addAPostInABlog(post, _isDraft.value, _account)) {
                is Result.Success -> {
                    _eventFlow.emit(
                        UiEvent.OpenPostInBrowserWithSnackBar(
                            message = "Post is published${if (isDraft.value) " as draft" else ""} at ${publishedPostResult.value.url} Do you want to open it in browser?",
                            link = publishedPostResult.value.url
                        )
                    )
                    uploadPostBtnState.value = ButtonState.COMPLETED
                }
                is Result.Failure -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar("Post could not be published : ${publishedPostResult.message}"))
                    uploadPostBtnState.value = ButtonState.INITIAL
                }
            }
        }
    }
}

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class OpenPostInBrowserWithSnackBar(val message: String, val link: String) : UiEvent()
}