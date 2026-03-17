package ly.img.editor.configuration.photo.callback

import kotlinx.coroutines.CancellationException
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.engine.MimeType
import java.nio.ByteBuffer

suspend fun PhotoConfigurationBuilder.onExport(
    preExport: suspend PhotoConfigurationBuilder.() -> Unit = {
        onPreExport()
    },
    exportByteBuffer: suspend PhotoConfigurationBuilder.() -> ByteBuffer = {
        onExportByteBuffer()
    },
    postExport: suspend PhotoConfigurationBuilder.(ByteBuffer) -> Unit = {
        onPostExport(it)
    },
    error: suspend PhotoConfigurationBuilder.(Exception) -> Unit = {
        onExportError(it)
    },
    finally: suspend PhotoConfigurationBuilder.() -> Unit = {
        onExportFinally()
    },
) {
    try {
        preExport()
        val result = exportByteBuffer()
        postExport(result)
    } catch (exception: Exception) {
        error(exception)
    } finally {
        finally()
    }
}

fun PhotoConfigurationBuilder.onPreExport() {
    showLoading = true
}

// highlight-starter-kit-photo-on-export-byte-buffer
suspend fun PhotoConfigurationBuilder.onExportByteBuffer(): ByteBuffer = export(
    block = requireNotNull(editorContext.engine.scene.get()),
    mimeType = MimeType.PNG,
)
// highlight-starter-kit-photo-on-export-byte-buffer

// highlight-starter-kit-photo-on-post-export
suspend fun PhotoConfigurationBuilder.onPostExport(byteBuffer: ByteBuffer) {
    val file = writeToFile(byteBuffer = byteBuffer, mimeType = MimeType.PNG)
    shareFile(file = file, mimeType = MimeType.PNG)
}
// highlight-starter-kit-photo-on-post-export

fun PhotoConfigurationBuilder.onExportError(error: Exception) {
    if (error is CancellationException) {
        throw error
    }
    this.error = error
}

fun PhotoConfigurationBuilder.onExportFinally() {
    showLoading = false
}
