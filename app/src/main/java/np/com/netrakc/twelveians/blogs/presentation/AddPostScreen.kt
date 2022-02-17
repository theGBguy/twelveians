package np.com.netrakc.twelveians.blogs.presentation

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.blogger.BloggerScopes
import kotlinx.coroutines.flow.collectLatest
import np.com.netrakc.twelveians.blogs.presentation.components.ButtonState
import np.com.netrakc.twelveians.blogs.presentation.components.LoadingButton
import np.com.netrakc.twelveians.blogs.presentation.utils.PerformGoogleSignIn
import np.com.netrakc.twelveians.blogs.presentation.utils.getFileFromUri
import java.io.File

@Composable
fun AddPostScreen() {
    val viewModel: AddPostViewModel = hiltViewModel()
    val context = LocalContext.current

    var account: Account?

    // const val SERVER_AUTH_CODE = "776446342068-rp23dfupbh0p197acvehq3b3mchfrsbm.apps.googleusercontent.com"
    // create google sign in options to request permissions to manager blogger
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestScopes(Scope(BloggerScopes.BLOGGER))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    val signInLauncher = rememberLauncherForActivityResult(
        contract = PerformGoogleSignIn(),
        onResult = { task ->
            account = try {
                val signInAccount = task.getResult(ApiException::class.java)
                signInAccount.account
            } catch (e: ApiException) {
                Log.d("AddPostScreen", "Sign In Error : $e")
                null
            }
            viewModel.onEvent(AddPostScreenEvent.OnSignInResult(account))
        }
    )

    val scaffoldState = rememberScaffoldState()
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            viewModel.onEvent(AddPostScreenEvent.OnImageLauncherResult(uris))
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Long
                    )
                }
                is UiEvent.OpenPostInBrowserWithSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Long,
                        actionLabel = "Ok"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(event.link)
                        })
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LoadingButton(
                modifier = Modifier,
                currentState = viewModel.signInBtnState,
                onClick = {
                    signInLauncher.launch(googleSignInClient)
                }
            )

            if (viewModel.signInBtnState.value == ButtonState.COMPLETED) {
                Text(
                    text = "Let's create a new post for TWELVEIANS",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Button(onClick = {
                    imageLauncher.launch("image/*")
                }, content = {
                    Text(text = "Select post content images")
                })

                if (viewModel.imagesUri.value.isNotEmpty()) {
                    Text(
                        text = "Selected Images (${viewModel.imagesUri.value.size})",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(items = viewModel.imagesUri.value) { uri ->
                            CustomImage(
                                context = context,
                                uri = uri,
                                onDeleteClick = { deletedUri: Uri ->
                                    viewModel.onEvent(
                                        AddPostScreenEvent.OnImageDeleteClick(deletedUri)
                                    )
                                }
                            )
                        }
                    }

                    LoadingButton(
                        modifier = Modifier,
                        currentState = viewModel.uploadImageBtnState,
                        initialText = "Upload images to Imgur",
                        loadingText = "Uploading images",
                        completedText = "Images uploaded",
                        onClick = {
                            val files: List<File> = viewModel.imagesUri.value.map { uri ->
                                getFileFromUri(context, uri)
                            }
                            viewModel.onEvent(AddPostScreenEvent.OnUploadImagesBtnClick(files))
                        }
                    )

                    if (viewModel.uploadImageBtnState.value == ButtonState.COMPLETED) {
                        Text(
                            text = "Let's give your post a name! Yahoo!",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = viewModel.contentTitle.value,
                            onValueChange = {
                                viewModel.onEvent(AddPostScreenEvent.OnContentTitleChange(it))
                            }
                        )

                        Text(
                            text = "Let's add tags to your post!",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = viewModel.contentTags.value,
                            onValueChange = {
                                viewModel.onEvent(AddPostScreenEvent.OnContentTagsChange(it))
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            Checkbox(
                                checked = viewModel.isDraft.value,
                                onCheckedChange = {
                                    viewModel.onEvent(AddPostScreenEvent.OnIsDraftCheckChange(it))
                                }
                            )
                            Text("Draft")
                        }

                        LoadingButton(
                            modifier = Modifier,
                            currentState = viewModel.uploadPostBtnState,
                            initialText = "Upload post",
                            loadingText = "Uploading post to Blogger",
                            completedText = "Post uploaded",
                            onClick = {
                                viewModel.onEvent(AddPostScreenEvent.OnUploadPostClick)
                            }
                        )
                    }
                }
            }
            Text(text = "Made with \uD83D\uDC9C by Chiranjeevi Pandey")
        }
    }
}

@Composable
fun CustomImage(
    context: Context,
    uri: Uri,
    onDeleteClick: (Uri) -> Unit
) {
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.setTargetSampleSize(10)
        }
    }

    bitmap?.let {
        Box {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { onDeleteClick(uri) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(
                        color = MaterialTheme.colors.surface.copy(
                            alpha = 0.5f
                        ),
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .width(120.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete this image",
                    tint = MaterialTheme.colors.onSurface.copy(
                        alpha = 0.7f
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPostScreenPreview() {
    AddPostScreen()
}