package np.com.netrakc.twelveians.blogs.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import np.com.netrakc.twelveians.blogs.ui.theme.TwelveiansTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwelveiansTheme {
                AddPostScreen()
            }
        }
    }
}