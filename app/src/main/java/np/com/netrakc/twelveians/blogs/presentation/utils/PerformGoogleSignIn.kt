package np.com.netrakc.twelveians.blogs.presentation.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task

class PerformGoogleSignIn :
    ActivityResultContract<GoogleSignInClient, Task<GoogleSignInAccount>>() {
    override fun createIntent(context: Context, input: GoogleSignInClient): Intent {
        return input.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(intent.takeIf { resultCode == RESULT_OK })
    }
}