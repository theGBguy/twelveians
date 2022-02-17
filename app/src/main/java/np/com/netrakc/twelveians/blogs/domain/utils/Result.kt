package np.com.netrakc.twelveians.blogs.domain.utils

sealed class Result<out T> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Failure(val message: String?) : Result<Nothing>()
}

