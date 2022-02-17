package np.com.netrakc.blogs

import np.com.netrakc.twelveians.blogs.presentation.utils.createPostHTML
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun generateHtmlContent() {
        println(
            createPostHTML(
                title = "This is title",
                imageUrls = listOf("https://i.imgur.com/o2AySZI.jpeg"),
                tags = "class 12, grade 12, physics"
            )
        )
    }
}