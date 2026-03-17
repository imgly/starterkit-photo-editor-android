package ly.img.starterkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import ly.img.editor.Editor
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.core.configuration.EditorConfiguration
import ly.img.editor.core.configuration.remember

class EditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is required to remove the default action bar on top.
        setTheme(android.R.style.Theme_Material_Light_NoActionBar)
        // This is required, so that the editor is displayed full screen on relatively older devices.
        enableEdgeToEdge()
        setContent {
            PhotoEditor {
                // Close the editor, ignore any errors.
                finish()
            }
        }
    }
}

@Composable
private fun PhotoEditor(onClose: (error: Throwable?) -> Unit) {
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder)
        },
        onClose = onClose,
    )
}
