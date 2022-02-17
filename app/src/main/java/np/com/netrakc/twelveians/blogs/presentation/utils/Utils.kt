package np.com.netrakc.twelveians.blogs.presentation.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun getFileFromUri(context: Context, uri: Uri): File {
    val outputFile = File.createTempFile("temp", null)

    context.contentResolver.openInputStream(uri)
        ?.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    return outputFile
}

// https://i.imgur.com/o2AySZI.jpeg first image uploaded using this app :D
// since it was an anonymous upload so copied the url

fun createPostHTML(
    title: String = "",
    imageUrls: List<String> = listOf(),
    tags: String = ""
): String {
    return "<h1 style=\"text-align: left;\">&nbsp;$title</h1>" + // title
            "<div> <br/> </div>" + // line break but do we need <div> though?
            "<h2 style=\"text-align: center;\">" + // image solution
            "<span style=\"font-size: large;\">Image Solution:</span>" +
            "</h2>" +
            "<div>" + // container for images
            if (imageUrls.isNotEmpty()) {
                var imageContent = ""
                repeat(imageUrls.size) { index -> // tags for images are dynamically generated per images' number
                    imageContent += "<div class=\"separator\" style=\"clear: both; text-align: center;\">" +
                            "<a href=\"${imageUrls[index]}\" style =\"margin-left: 1em; margin-right: 1em;\">" +
                            "<img alt=\"$title\" border=\"0\" data-original-height=\"3031\" data-original-width=\"2176\" height=\"640\" src=\"${imageUrls[index]}\" title=\"$title\" width=\"460\" />" +
                            "</a>" +
                            "</div>" +
                            "<br/>"
                }
                imageContent
            } else {
                ""
            } +
            "</div>" + // container for images closing tag
            "<div>" + // container for tags
            if (tags.isNotBlank()) { // if tags are provided then only generate the HTML for them
                "<span style=\"font-size: medium;\">Tags: $tags</span>"// optional tags
            } else {
                ""
            } +
            "</div>"
}

fun createSamplePostContent(): String {
    return createPostHTML(
        title = "This is title",
        imageUrls = listOf("https://i.imgur.com/o2AySZI.jpeg"),
        tags = "class 12, grade 12, physics"
    )
}
