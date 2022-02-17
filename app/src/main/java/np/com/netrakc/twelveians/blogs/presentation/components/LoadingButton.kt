package np.com.netrakc.twelveians.blogs.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ButtonState {
    INITIAL, LOADING, COMPLETED
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    currentState: MutableState<ButtonState>,
    initialText: String = "Sign in",
    loadingText: String = "Signing in",
    completedText: String = "Signed in",
    initialIcon: ImageVector = Icons.Rounded.Close,
    completedIcon: ImageVector = Icons.Rounded.Check,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.animateContentSize(),
        onClick = {
            currentState.value = ButtonState.LOADING
            onClick()
        }
    ) {
        Text(
            text = when (currentState.value) {
                ButtonState.INITIAL -> initialText
                ButtonState.LOADING -> loadingText
                ButtonState.COMPLETED -> completedText
            }
        )
        Spacer(Modifier.width(16.dp))
        AnimatedVisibility(visible = currentState.value == ButtonState.LOADING) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onPrimary
            )
        }
        AnimatedVisibility(visible = currentState.value != ButtonState.LOADING) {
            Icon(
                imageVector = if (currentState.value == ButtonState.INITIAL) {
                    initialIcon
                } else {
                    completedIcon
                },
                contentDescription = "Not signed in",
                tint = if (currentState.value == ButtonState.INITIAL) {
                    Color.Red.copy(alpha = .7f)
                } else {
                    Color.Green.copy(alpha = .7f)
                }
            )
        }
    }
}

@Preview
@Composable
fun LoadingButtonPreview() {
    val state = remember { mutableStateOf(ButtonState.INITIAL) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state) {
        coroutineScope.launch {
            delay(5000)
            state.value = ButtonState.COMPLETED
        }
    }
    LoadingButton(
        currentState = state,
        onClick = { state.value = ButtonState.LOADING }
    )
}
